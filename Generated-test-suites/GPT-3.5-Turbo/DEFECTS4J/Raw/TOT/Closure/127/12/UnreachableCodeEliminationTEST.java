import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

    private UnreachableCodeElimination uce;

    @Before
    public void setUp() {
        uce = new UnreachableCodeElimination();
    }

    @Test
    public void testProcess_ReturnStatement() {
        String input = "function test() {\n" +
                "  return;\n" +
                "  alert('unreachable');\n" +
                "}";
        String expectedOutput = "function test() {\n" +
                "  return;\n" +
                "}";
        
        assertEquals(expectedOutput, uce.process(input));
    }

    @Test
    public void testProcess_NoOpStatements() {
        String input = "function test() {\n" +
                "  a.b.MyClass.prototype.propertyName;\n" +
                "  true;\n" +
                "}";
        String expectedOutput = "function test() {\n" +
                "  \n" +
                "}";
        
        assertEquals(expectedOutput, uce.process(input));
    }

    @Test
    public void testProcess_MultipleMethods() {
        String input = "function test1() {\n" +
                "  return;\n" +
                "  alert('unreachable');\n" +
                "}\n" +
                "\n" +
                "function test2() {\n" +
                "  a.b.MyClass.prototype.propertyName;\n" +
                "  true;\n" +
                "}\n" +
                "\n" +
                "function test3() {\n" +
                "  return;\n" +
                "  alert('unreachable');\n" +
                "}";
        String expectedOutput = "function test1() {\n" +
                "  return;\n" +
                "}\n" +
                "\n" +
                "function test2() {\n" +
                "  \n" +
                "}\n" +
                "\n" +
                "function test3() {\n" +
                "  return;\n" +
                "}";
        
        assertEquals(expectedOutput, uce.process(input));
    }

    @Test
    public void testProcess_DeeplyNestedStatements() {
        String input = "function test() {\n" +
                "  if (x) {\n" +
                "    if (y) {\n" +
                "      if (z) {\n" +
                "        return;\n" +
                "        alert('unreachable');\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        String expectedOutput = "function test() {\n" +
                "  if (x) {\n" +
                "    if (y) {\n" +
                "      if (z) {\n" +
                "        return;\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        
        assertEquals(expectedOutput, uce.process(input));
    }

    @Test
    public void testProcess_ReturnStatementWithExpression() {
        String input = "function test() {\n" +
                "  return 1 + 2;\n" +
                "}";
        String expectedOutput = "function test() {\n" +
                "  return 1 + 2;\n" +
                "}";
        
        assertEquals(expectedOutput, uce.process(input));
    }

    @Test
    public void testProcess_ComplexExample() {
        String input = "function test() {\n" +
                "  if (x) {\n" +
                "    if (y) {\n" +
                "      if (z) {\n" +
                "        return 1 + 2;\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  a.b.MyClass.prototype.propertyName;\n" +
                "  true;\n" +
                "}";
        String expectedOutput = "function test() {\n" +
                "  if (x) {\n" +
                "    if (y) {\n" +
                "      if (z) {\n" +
                "        return 1 + 2;\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        
        assertEquals(expectedOutput, uce.process(input));
    }

    @Test
    public void testProcess_NoDeadCode() {
        String input = "function test() {\n" +
                "  alert('reachable');\n" +
                "  return 1 + 2;\n" +
                "}";
        String expectedOutput = "function test() {\n" +
                "  alert('reachable');\n" +
                "  return 1 + 2;\n" +
                "}";
        
        assertEquals(expectedOutput, uce.process(input));
    }

    @Test
    public void testProcess_EmptyFunction() {
        String input = "function test() {}";
        String expectedOutput = "function test() {}";
        
        assertEquals(expectedOutput, uce.process(input));
    }

    @Test
    public void testProcess_FunctionWithBlock() {
        String input = "function test() {\n" +
                "  {\n" +
                "    return;\n" +
                "    alert('unreachable');\n" +
                "  }\n" +
                "}";
        String expectedOutput = "function test() {\n" +
                "  {\n" +
                "    return;\n" +
                "  }\n" +
                "}";
        
        assertEquals(expectedOutput, uce.process(input));
    }

    @Test
    public void testProcess_NullInput() {
        String input = null;
        String expectedOutput = "";
        
        assertEquals(expectedOutput, uce.process(input));
    }

    @Test
    public void testProcess_EmptyInput() {
        String input = "";
        String expectedOutput = "";
        
        assertEquals(expectedOutput, uce.process(input));
    }

    @Test
    public void testProcess_MultipleInputs() {
        String input1 = "function test1() {\n" +
                "  return;\n" +
                "  alert('unreachable');\n" +
                "}";
        String expectedOutput1 = "function test1() {\n" +
                "  return;\n" +
                "}";
        
        assertEquals(expectedOutput1, uce.process(input1));
        
        String input2 = "function test2() {\n" +
                "  a.b.MyClass.prototype.propertyName;\n" +
                "  true;\n" +
                "}";
        String expectedOutput2 = "function test2() {\n" +
                "  \n" +
                "}";
        
        assertEquals(expectedOutput2, uce.process(input2));
    }

    @Test
    public void testProcess_LargeInput() {
        String input = "function test() {\n" +
                "  if (x) {\n" +
                "    if (y) {\n" +
                "      if (z) {\n" +
                "        return 1 + 2;\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  a.b.MyClass.prototype.propertyName;\n" +
                "  true;\n" +
                "  alert('unreachable');\n" +
                "  return 3 + 4;\n" +
                "}";
        String expectedOutput = "function test() {\n" +
                "  if (x) {\n" +
                "    if (y) {\n" +
                "      if (z) {\n" +
                "        return 1 + 2;\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        
        assertEquals(expectedOutput, uce.process(input));
    }

}