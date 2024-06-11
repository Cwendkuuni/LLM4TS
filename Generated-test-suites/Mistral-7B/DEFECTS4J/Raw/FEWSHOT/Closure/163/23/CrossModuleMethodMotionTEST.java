package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.анаlyzePrototypeProperties.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.анаlyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.анаlyzePrototypeProperties.Property;
import com.google.javascript.jscomp.анаlyzePrototypeProperties.Symbol;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.logging.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class CrossModuleMethodMotionTest {

    private static final String STUB_METHOD_NAME = "JSCompiler_stubMethod";
    private static final String UNSTUB_METHOD_NAME = "JSCompiler_unstubMethod";

    @Test
    public void testMoveMethods() {
        // Given
        Compiler compiler = mock(Compiler.class);
        IdGenerator idGenerator = mock(IdGenerator.class);
        Logger logger = mock(Logger.class);
        JSModule moduleA = mock(JSModule.class);
        JSModule moduleB = mock(JSModule.class);
        AnalyzePrototypeProperties analyzer = new AnalyzePrototypeProperties(compiler, null, false, false);
        CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);

        when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph() {
            private List<JSModule> modules = new ArrayList<>();

            @Override
            public int getModuleCount() {
                return modules.size();
            }

            @Override
            public JSModule getModule(String name) {
                return modules.stream().filter(jsModule -> jsModule.getName().equals(name))
                        .findFirst().orElse(null);
            }

            @Override
            public void addModule(JSModule module) {
                modules.add(module);
            }
        });

        when(compiler.parseSyntheticCode(anyString())).then((InvocationOnMock invocationOnMock) -> {
            String code = invocationOnMock.getArgumentAt(0, String.class);
            return Node.externalCall(code);
        });

        when(compiler.getNodeForCodeInsertion(null)).then((InvocationOnMock invocationOnMock) -> {
            Node node = invocationOnMock.getArgumentAt(0, Node.class);
            return node;
        });

        when(compiler.getNodeForCodeInsertion(moduleA)).then((InvocationOnMock invocationOnMock) -> {
            Node node = invocationOnMock.getArgumentAt(0, Node.class);
            return new Node() {
                @Override
                public Node getParent() {
                    return node;
                }

                @Override
                public void addChildToFront(Node child) {
                }
            };
        });

        when(moduleA.getName()).thenReturn("moduleA");
        when(moduleB.getName()).thenReturn("moduleB");

        when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);
        when(idGenerator.newId()).thenReturn(1);

        NameInfo nameInfo = new NameInfo() {
            @Override
            public Collection<Symbol> getDeclarations() {
                return Collections.singletonList(new Property() {
                    @Override
                    public Node getValue() {
                        return new Node() {
                            @Override
                            public Node getParent() {
                                return new Node() {
                                    @Override
                                    public NodeType getType() {
                                        return NodeType.Get;
                                    }
                                };
                            }
                        };
                    }

                    @Override
                    public String getName() {
                        return "method";
                    }

                    @Override
                    public JSModule getModule() {
                        return moduleA;
                    }

                    @Override
                    public Node getPrototype() {
                        return new Node() {
                            @Override
                            public NodeType getType() {
                                return NodeType.Object;
                            }
                        };
                    }
                });
            }

            @Override
            public boolean isReferenced() {
                return true;
            }

            @Override
            public boolean readsClosureVariables() {
                return false;
            }

            @Override
            public JSModule getDeepestCommonModuleRef() {
                return moduleA;
            }
        };

        // When
        pass.moveMethods(Collections.singletonList(nameInfo));

        // Then
        verify(compiler, times(1)).report(eq(NULL_COMMON_MODULE_ERROR));
        verifyNoMoreInteractions(compiler);

        // Reset mocks
        reset(compiler);
        reset(idGenerator);
        reset(logger);
        reset(moduleA);
        reset(moduleB);

        when(idGenerator.hasGeneratedAnyIds()).thenReturn(true);

        // When
        pass.moveMethods(Collections.singletonList(nameInfo));

        // Then
        verify(compiler, times(1)).getNodeForCodeInsertion(eq(moduleB));
        verify(compiler, times(1)).parseSyntheticCode(anyString());
        verify(compiler, times(1)).reportCodeChange();
        verifyNoMoreInteractions(compiler);

        // Reset mocks
        reset(compiler);
        reset(idGenerator);
        reset(logger);
        reset(moduleA);
        reset(moduleB);

        when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);
        when(moduleA.getName()).thenReturn("A");
        when(moduleB.getName()).thenReturn("B");

        when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph() {
            @Override
            public JSModule getModule(String name) {
                switch (name) {
                    case "A":
                        return moduleA;
                    case "B":
                        return moduleB;
                    default:
                        throw new IllegalArgumentException();
                }
            }

            @Override
            public void addModule(JSModule module) {
                if (!modules.contains(module)) {
                    modules.add(module);
                }
            }
        });

        Property property = new Property() {
            @Override
            public Node getValue() {
                return new Node() {
                    @Override
                    public NodeType getType() {
                        return NodeType.Function;
                    }
                };
            }

            @Override
            public String getName() {
                return "method";
            }

            @Override
            public JSModule getModule() {
                return moduleA;
            }

            @Override
            public Node getPrototype() {
                return new Node() {
                    @Override
                    public NodeType getType() {
                        return NodeType.Object;
                    }
                };
            }
        };

        NameInfo nameInfo1 = new NameInfo() {
            @Override
            public Collection<Symbol> getDeclarations() {
                return Collections.singletonList(property);
            }

            @Override
            public boolean isReferenced() {
                return true;
            }

            @Override
            public boolean readsClosureVariables() {
                return false;
            }

            @Override
            public JSModule getDeepestCommonModuleRef() {
                return moduleA;
            }
        };

        NameInfo nameInfo2 = new NameInfo() {
            @Override
            public Collection<Symbol> getDeclarations() {
                return Collections.emptyList();
            }

            @Override
            public boolean isReferenced() {
                return true;
            }

            @Override
            public boolean readsClosureVariables() {
                return false;
            }

            @Override
            public JSModule getDeepestCommonModuleRef() {
                return null;
            }
        };

        when(analyzer.getAllNameInfo()).thenReturn(new ArrayList<NameInfo>() {
            {
                add(nameInfo1);
                add(nameInfo2);
            }
        });

        // When
        pass.process(null, null);

        // Then
        verify(logger, times(1)).log(Level.FINE, "Moving methods...");
        verify(analyzer, times(1)).process(null, null);
        verify(pass, times(1)).moveMethods(eq(analyzer.getAllNameInfo()));
        verifyNoMoreInteractions(logger);
        verifyNoMoreInteractions(analyzer);
        verifyNoMoreInteractions(pass);
    }
}