package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

    @Test
    public void testProcess() {
        // Define the input and expected output Nodes
        Node inputExterns = new Node();
        Node inputRoot = new Node();
        Node expectedExterns = new Node();
        Node expectedRoot = new Node();

        // Create the Compiler
        Compiler inputCompiler = new Compiler();
        Compiler expectedCompiler = new Compiler();

        // Create the RemoveConstantExpressions instances
        RemoveConstantExpressions inputInstance = new RemoveConstantExpressions(inputCompiler);
        RemoveConstantExpressions expectedInstance = new RemoveConstantExpressions(expectedCompiler);

        // Invoke the process() method
        inputInstance.process(inputExterns, inputRoot);
        expectedInstance.process(expectedExterns, expectedRoot);

        // Compare the resulting Nodes
        assertEquals(expectedExterns, inputExterns);
        assertEquals(expectedRoot, inputRoot);
    }

    @Test
    public void testRemoveConstantRValuesCallback() {
        // Create the RemoveConstantExpressions instance
        RemoveConstantExpressions.RemoveConstantRValuesCallback instance = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

        // Define the input and expected Nodes
        Node inputParent = new Node();
        Node inputNode = new Node();
        Node expectedParent = new Node();
        Node expectedNode = new Node();

        // Invoke the trySimplify() method
        instance.trySimplify(inputParent, inputNode);

        // Compare the resulting Nodes
        assertEquals(expectedParent, inputParent);
        assertEquals(expectedNode, inputNode);
    }

    @Test
    public void testTrySimplify() {
        // Create the RemoveConstantExpressions instance
        RemoveConstantExpressions.RemoveConstantRValuesCallback instance = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

        // Define the input and expected Nodes
        Node inputParent = new Node();
        Node inputNode = new Node();
        Node expectedParent = new Node();
        Node expectedNode = new Node();

        // Invoke the trySimplify() method
        instance.trySimplify(inputParent, inputNode);

        // Compare the resulting Nodes
        assertEquals(expectedParent, inputParent);
        assertEquals(expectedNode, inputNode);
    }

    @Test
    public void testGetSideEffectNodes() {
        // Create the RemoveConstantExpressions instance
        RemoveConstantExpressions.RemoveConstantRValuesCallback instance = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

        // Define the input and expected Nodes
        Node inputNode = new Node();
        List<Node> expectedOutput = new ArrayList<>();

        // Invoke the getSideEffectNodes() method
        List<Node> output = instance.getSideEffectNodes(inputNode);

        // Compare the resulting Nodes
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testGetResult() {
        // Create the RemoveConstantExpressions instance
        RemoveConstantExpressions.RemoveConstantRValuesCallback instance = new RemoveConstantExpressions.RemoveConstantRValuesCallback();

        // Define the input and expected Results
        RemoveConstantExpressions.Result expectedOutput = new RemoveConstantExpressions.Result();

        // Invoke the getResult() method
        RemoveConstantExpressions.Result output = instance.getResult();

        // Compare the resulting Results
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testReportCodeHasChangedListener() {
        // Define the input Node
        Node inputNode = new Node();

        // Create the Result instance
        RemoveConstantExpressions.Result result = new RemoveConstantExpressions.Result();

        // Create the ReportCodeHasChangedListener instance
        RemoveConstantExpressions.ReportCodeHasChangedListener instance = new RemoveConstantExpressions.ReportCodeHasChangedListener(result);

        // Invoke the nodeRemoved() method
        instance.nodeRemoved(inputNode);

        // Compare the changed field in the Result instance
        assertTrue(result.changed);
    }
}