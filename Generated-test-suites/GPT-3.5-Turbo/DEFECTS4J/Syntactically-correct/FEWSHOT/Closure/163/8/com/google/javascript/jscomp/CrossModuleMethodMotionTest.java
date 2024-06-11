package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {
    
    @Mock
    AbstractCompiler compiler;
    
    @Mock
    IdGenerator idGenerator;
    
    @Mock
    AnalyzePrototypeProperties analyzer;
    
    @Mock
    JSModuleGraph moduleGraph;
    
    CrossModuleMethodMotion crossModuleMethodMotion;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);
        crossModuleMethodMotion.moduleGraph = moduleGraph;
        crossModuleMethodMotion.analyzer = analyzer;
    }

    @Test
    public void testProcess() {
        Node externRoot = IR.empty();
        Node root = IR.empty();

        // Test when module graph has less than 2 modules
        when(moduleGraph.getModuleCount()).thenReturn(1);
        crossModuleMethodMotion.process(externRoot, root);
        verifyZeroInteractions(analyzer);

        // Test when deepestCommonModuleRef is null
        when(moduleGraph.getModuleCount()).thenReturn(2);
        crossModuleMethodMotion.process(externRoot, root);
        verify(compiler).report(any(JSError.class));

        // Test normal scenario
        when(moduleGraph.getModuleCount()).thenReturn(2);
        when(analyzer.getAllNameInfo()).thenReturn(anyList());
        crossModuleMethodMotion.process(externRoot, root);
        verify(analyzer).process(externRoot, root);
        verify(analyzer).getAllNameInfo();
        verify(compiler).reportCodeChange();
    }

    @Test
    public void testMoveMethods() {
        Collection<NameInfo> allNameInfo = anyCollection();
        // Test when nameInfo is not referenced
        NameInfo nameInfo = mock(NameInfo.class);
        when(nameInfo.isReferenced()).thenReturn(false);
        allNameInfo.add(nameInfo);

        crossModuleMethodMotion.moveMethods(allNameInfo);
        verifyZeroInteractions(nameInfo);

        // Test when deepestCommonModuleRef is null
        when(nameInfo.isReferenced()).thenReturn(true);
        when(nameInfo.getDeepestCommonModuleRef()).thenReturn(null);
        allNameInfo.clear();
        allNameInfo.add(nameInfo);

        crossModuleMethodMotion.moveMethods(allNameInfo);
        verify(compiler).report(any(JSError.class));

        // Test normal scenario
        JSModule deepestCommonModuleRef = mock(JSModule.class);
        Symbol symbol = mock(Property.class);
        Node value = IR.function(IR.name("fn"), IR.paramList(), IR.block());
        Node valueParent = IR.empty();
        Node proto = IR.name("proto");

        // Stubbing
        when(nameInfo.isReferenced()).thenReturn(true);
        when(nameInfo.getDeepestCommonModuleRef()).thenReturn(deepestCommonModuleRef);
        when(nameInfo.getDeclarations()).thenReturn(anyList().descendingIterator());
        when(symbol.getValue()).thenReturn(value);
        when(symbol.getPrototype()).thenReturn(proto);
        when(value.getParent()).thenReturn(valueParent);
        when(moduleGraph.dependsOn(deepestCommonModuleRef, any(JSModule.class))).thenReturn(true);
        when(idGenerator.newId()).thenReturn(1);

        allNameInfo.clear();
        allNameInfo.add(nameInfo);

        crossModuleMethodMotion.moveMethods(allNameInfo);
        verify(compiler).getNodeForCodeInsertion(deepestCommonModuleRef);
        verify(compiler).reportCodeChange();
    }

    @Test
    public void testIdGenerator() {
        CrossModuleMethodMotion.IdGenerator idGenerator = crossModuleMethodMotion.new IdGenerator();

        // Test hasGeneratedAnyIds() returns false initially
        assertEquals(false, idGenerator.hasGeneratedAnyIds());

        // Test hasGeneratedAnyIds() returns true when an id is generated
        idGenerator.newId();
        assertEquals(true, idGenerator.hasGeneratedAnyIds());

        // Test hasGeneratedAnyIds() returns true when multiple ids are generated
        idGenerator.newId();
        idGenerator.newId();
        assertEquals(true, idGenerator.hasGeneratedAnyIds());
    }

}