import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenameLabelsTest {

    private RenameLabels renameLabels;

    @Before
    public void setUp() {
        renameLabels = new RenameLabels();
    }

    @Test
    public void testProcess_LabelWithUnusedName_ShouldRemoveLabelNode() {
        String input = "function foo() { label: return 5; }";
        String expected = "function foo() {  return 5; }";
        assertEquals(expected, renameLabels.process(input));
    }

    @Test
    public void testProcess_LabelWithUsedName_ShouldRenameLabelNode() {
        String input = "function foo() { a: return 5; }";
        String expected = "function foo() { b: return 5; }";
        assertEquals(expected, renameLabels.process(input));
    }

    @Test
    public void testProcess_LabelWithNestedLabels_ShouldRenameLabels() {
        String input = "function foo() { a: { b: { return 5; } } }";
        String expected = "function foo() { c: { d: { return 5; } } }";
        assertEquals(expected, renameLabels.process(input));
    }

    @Test
    public void testProcess_ContinueWithUnusedLabel_ShouldRemoveLabelNodeAndPreserveContinue() {
        String input = "function foo() { continue label; }";
        String expected = "function foo() { continue; }";
        assertEquals(expected, renameLabels.process(input));
    }

    @Test
    public void testProcess_ContinueWithUsedLabel_ShouldRenameLabelAndPreserveContinue() {
        String input = "function foo() { continue label; }";
        String expected = "function foo() { continue renamedLabel; }";
        assertEquals(expected, renameLabels.process(input));
    }

    @Test
    public void testProcess_BreakWithUnusedLabel_ShouldRemoveLabelNodeAndPreserveBreak() {
        String input = "function foo() { break label; }";
        String expected = "function foo() { break; }";
        assertEquals(expected, renameLabels.process(input));
    }

    @Test
    public void testProcess_BreakWithUsedLabel_ShouldRenameLabelAndPreserveBreak() {
        String input = "function foo() { break label; }";
        String expected = "function foo() { break renamedLabel; }";
        assertEquals(expected, renameLabels.process(input));
    }

    @Test
    public void testProcess_MultipleLabels_SameDepth_ShouldRenameDistinctly() {
        String input = "function foo() { a: return a; a: return a; }";
        String expected = "function foo() { b: return b; c: return c; }";
        assertEquals(expected, renameLabels.process(input));
    }

    @Test
    public void testProcess_MultipleLabels_DifferentDepth_ShouldRenameDistinctly() {
        String input = "function foo() { a: return a; a: { b: return b; } }";
        String expected = "function foo() { c: return c; d: { e: return e; } }";
        assertEquals(expected, renameLabels.process(input));
    }

    @Test
    public void testProcess_EmptyFunctionBody_ShouldNotModifyCode() {
        String input = "function foo() {}";
        String expected = "function foo() {}";
        assertEquals(expected, renameLabels.process(input));
    }

    @Test
    public void testProcess_FunctionWithNonLabelNodes_ShouldNotModifyCode() {
        String input = "function foo() { var a = 5; return a; }";
        String expected = "function foo() { var a = 5; return a; }";
        assertEquals(expected, renameLabels.process(input));
    }

    @Test
    public void testProcess_EmptyInput_ShouldNotModifyCode() {
        String input = "";
        String expected = "";
        assertEquals(expected, renameLabels.process(input));
    }
}