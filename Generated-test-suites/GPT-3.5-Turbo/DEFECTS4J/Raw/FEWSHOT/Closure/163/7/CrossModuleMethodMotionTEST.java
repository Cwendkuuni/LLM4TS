package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AnalyzePrototypeProperties.Property;
import com.google.javascript.jscomp.CrossModuleMethodMotion.IdGenerator;
import com.google.javascript.jscomp.CrossModuleMethodMotion.Symbol;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.PropertyAssignProperty;
import com.google.javascript.jscomp.PropertyReferenceGraph;

public class CrossModuleMethodMotionTest {

    private AbstractCompiler compiler;
    private IdGenerator idGenerator;
    private boolean canModifyExterns;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        idGenerator = mock(IdGenerator.class);
        canModifyExterns = false;
    }

    @Test
    public void testProcess() {
        // Set up test data
        Node externRoot = createNode();
        Node root = createNode();
        CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);

        // Set up mock objects
        AnalyzePrototypeProperties mockAnalyzer = mock(AnalyzePrototypeProperties.class);
        JSModuleGraph mockModuleGraph = mock(JSModuleGraph.class);
        PassConfig mockPassConfig = mock(PassConfig.class);

        // Mock method calls
        when(compiler.getModuleGraph()).thenReturn(mockModuleGraph);
        when(mockModuleGraph.getModuleCount()).thenReturn(2);
        when(idGenerator.hasGeneratedAnyIds()).thenReturn(true);
        when(mockAnalyzer.getAllNameInfo()).thenReturn(new HashSet<>());
        when(mockPassConfig.getPropFn()).thenReturn(PropertyReferenceGraph.UNCOMPUTED_METHODS);
        when(mockPassConfig.memberFn()).thenReturn(mock(MemberFunctionMotion.SeparateSingletonMethods.class));

        // Run method to be tested
        pass.process(externRoot, root);

        // Verify results
        verify(compiler).getNodeForCodeInsertion(null);
        verify(mockModuleGraph).getModuleCount();
        verify(mockModuleGraph, times(2)).dependsOn(any(), any());
        verify(mockAnalyzer).getAllNameInfo();
    }

    private Node createNode() {
        return new Node(null);
    }

    @Test
    public void testMoveMethods() {
        // Set up test data
        Collection<NameInfo> allNameInfo = createNameInfoCollection();
        CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);

        // Set up mock objects
        JSModule mockModule = mock(JSModule.class);
        Symbol mockSymbol = mock(Symbol.class);
        Property mockProperty = mock(Property.class);
        Node mockValue = mock(Node.class);
        Node mockValueParent = mock(Node.class);
        Node mockProto = mock(Node.class);
        Node mockStubCall = mock(Node.class);
        Node mockUnstubCall = mock(Node.class);
        Node mockUnstubParent = mock(Node.class);
        IdGenerator mockIdGenerator = mock(IdGenerator.class);

        // Set up name info
        NameInfo nameInfo = new NameInfo(mockModule, mockSymbol);
        nameInfo.setDeepestCommonModuleRef(mockModule);
        nameInfo.getDeclarations().add(mockProperty);
        allNameInfo.add(nameInfo);

        // Set up property
        when(mockProperty.getValue()).thenReturn(mockValue);
        when(mockProperty.getPrototype()).thenReturn(mockProto);

        // Set up value
        when(mockValue.isFunction()).thenReturn(true);
        when(mockValue.getParent()).thenReturn(mockValueParent);

        // Set up module graph
        JSModuleGraph mockModuleGraph = mock(JSModuleGraph.class);
        when(compiler.getModuleGraph()).thenReturn(mockModuleGraph);
        when(mockModuleGraph.dependsOn(any(), any())).thenReturn(true);

        // Set up id generator
        when(idGenerator.newId()).thenReturn(0);

        // Set up compiler
        when(compiler.getNodeForCodeInsertion(mockModule)).thenReturn(mockUnstubParent);

        // Set up stub call
        when(mockValue.copyInformationFromForTree(mockValue)).thenReturn(mockStubCall);
        when(mockValueParent.replaceChild(mockValue, mockStubCall)).thenReturn(true);

        // Set up unstub call
        when(compiler.reportCodeChange()).thenReturn(true);
        when(compiler.parseSyntheticCode(anyString())).thenReturn(createNode());
        when(compiler.getNodeForCodeInsertion(null)).thenReturn(mockNullNode(), mockNode(), mockUnstubParent);

        // Run method to be tested
        pass.moveMethods(allNameInfo);

        // Verify results
        verify(compiler, times(3)).getNodeForCodeInsertion(null);
        verify(mockValueParent, times(1)).replaceChild(mockValue, mockStubCall);
        verify(mockProto, times(1)).cloneTree();
        verify(mockUnstubParent, times(1)).addChildToFront(any());
    }

    private Collection<NameInfo> createNameInfoCollection() {
        return new ArrayList<>();
    }

    private Node mockNullNode() {
        return null;
    }

    private Node mockNode() {
        return new Node(null);
    }
}