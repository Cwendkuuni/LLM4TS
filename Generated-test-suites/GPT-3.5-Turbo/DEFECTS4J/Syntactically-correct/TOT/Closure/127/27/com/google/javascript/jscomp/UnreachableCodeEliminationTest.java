import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

    private UnreachableCodeElimination uce;

    @Before
    public void setUp() {
        uce = new UnreachableCodeElimination();
    }

    @After
    public void tearDown() {
        uce = null;
    }

    @Test
    public void testProcess_EmptyFunction_ReturnsEmptyFunction() {
        String input = "function test() {}";
        String expectedOutput = "function test() {}";
        String actualOutput = uce.process(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcess_FunctionWithNoOpStatement_ReturnsFunctionWithoutNoOpStatement() {
        String input = "function test() { a.b.MyClass.prototype.propertyName; }";
        String expectedOutput = "function test() {}";
        String actualOutput = uce.process(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcess_FunctionWithReturnStatement_ReturnsFunctionWithReturnStatementOnly() {
        String input = "function test() { return 5; alert('unreachable'); }";
        String expectedOutput = "function test() { return 5; }";
        String actualOutput = uce.process(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcess_FunctionWithMultipleReturnStatements_ReturnsFunctionWithLastReturnStatement() {
        String input = "function test() { if (x) { return 5; alert('unreachable'); } else { return 10; } }";
        String expectedOutput = "function test() { if (x) { return 5; } else { return 10; } }";
        String actualOutput = uce.process(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcess_FunctionWithDeadCodeAfterLoop_ReturnsFunctionWithoutDeadCode() {
        String input = "function test() { for (var i = 0; i < 10; i++) { if (i == 5) { return; alert('unreachable'); } } }";
        String expectedOutput = "function test() { for (var i = 0; i < 10; i++) { if (i == 5) { return; } } }";
        String actualOutput = uce.process(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcess_FunctionWithUselessBreak_ReturnsFunctionWithoutUselessBreak() {
        String input = "function test() { while (true) { if (x) { break; alert('unreachable'); } } }";
        String expectedOutput = "function test() { while (true) { if (x) { break; } } }";
        String actualOutput = uce.process(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcess_FunctionWithUselessReturn_ReturnsFunctionWithoutUselessReturn() {
        String input = "function test() { while (true) { if (x) { return; alert('unreachable'); } } }";
        String expectedOutput = "function test() { while (true) { if (x) { return; } } }";
        String actualOutput = uce.process(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testProcess_FunctionWithUselessContinue_ReturnsFunctionWithoutUselessContinue() {
        String input = "function test() { while (true) { if (x) { continue; alert('unreachable'); } } }";
        String expectedOutput = "function test() { while (true) { if (x) { continue; } } }";
        String actualOutput = uce.process(input);
        assertEquals(expectedOutput, actualOutput);
    }

}