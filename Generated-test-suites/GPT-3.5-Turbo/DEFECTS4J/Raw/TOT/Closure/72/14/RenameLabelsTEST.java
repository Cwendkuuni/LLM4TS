import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RenameLabelsTest {

    private AbstractCompiler compiler;
    private CompilerOptions options;

    @Before
    public void setUp() {
        compiler = new Compiler();
        options = new CompilerOptions();
    }

    @After
    public void tearDown() {
        compiler = null;
        options = null;
    }

    @Test
    public void testProcess_withRenamableLabels_renamesLabels() {
        // Arrange
        String input = "function foo() { a: { b: { } } }";
        String expectedOutput = "function foo() { a1: { b1: { } } }";
        JSSourceFile inputSourceFile = JSSourceFile.fromCode("input.js", input);
        List<JSSourceFile> sourceFiles = new ArrayList<>();
        sourceFiles.add(inputSourceFile);
        compiler.compile(externs, sourceFiles, options);
        RenameLabels renameLabels = new RenameLabels(compiler);

        // Act
        renameLabels.process(compiler.getRoot(), compiler);

        // Assert
        Result result = compiler.getResult();
        assertFalse(result.errors.length > 0);
        assertEquals(expectedOutput, compiler.toSource());
    }

    @Test
    public void testProcess_withUnrenamableLabels_doesNotRenameLabels() {
        // Arrange
        String input = "function foo() { a: { b: { } } }";
        JSSourceFile inputSourceFile = JSSourceFile.fromCode("input.js", input);
        List<JSSourceFile> sourceFiles = new ArrayList<>();
        sourceFiles.add(inputSourceFile);
        compiler.compile(externs, sourceFiles, options);
        RenameLabels renameLabels = new RenameLabels(compiler);

        // Act
        renameLabels.process(compiler.getRoot(), compiler);

        // Assert
        Result result = compiler.getResult();
        assertFalse(result.errors.length > 0);
        assertEquals(input, compiler.toSource());
    }

    @Test
    public void testProcess_withUndefinedLabelReference_removesTheLabel() {
        // Arrange
        String input = "function foo() { a: { b: break c; } }";
        String expectedOutput = "function foo() { break c; }";
        JSSourceFile inputSourceFile = JSSourceFile.fromCode("input.js", input);
        List<JSSourceFile> sourceFiles = new ArrayList<>();
        sourceFiles.add(inputSourceFile);
        compiler.compile(externs, sourceFiles, options);
        RenameLabels renameLabels = new RenameLabels(compiler);

        // Act
        renameLabels.process(compiler.getRoot(), compiler);

        // Assert
        Result result = compiler.getResult();
        assertFalse(result.errors.length > 0);
        assertEquals(expectedOutput, compiler.toSource());
    }

    @Test
    public void testProcess_withNonemptyLabel_doesNotRemoveTheLabel() {
        // Arrange
        String input = "function foo() { a: { b: { } } }";
        String expectedOutput = "function foo() { a: { b: { } } }";
        JSSourceFile inputSourceFile = JSSourceFile.fromCode("input.js", input);
        List<JSSourceFile> sourceFiles = new ArrayList<>();
        sourceFiles.add(inputSourceFile);
        compiler.compile(externs, sourceFiles, options);
        RenameLabels renameLabels = new RenameLabels(compiler);

        // Act
        renameLabels.process(compiler.getRoot(), compiler);

        // Assert
        Result result = compiler.getResult();
        assertFalse(result.errors.length > 0);
        assertEquals(expectedOutput, compiler.toSource());
    }

    @Test
    public void testProcess_withLabelNotReferencedInBreakOrContinue_removesTheLabel() {
        // Arrange
        String input = "function foo() { a: { b: } }";
        String expectedOutput = "function foo() { }";
        JSSourceFile inputSourceFile = JSSourceFile.fromCode("input.js", input);
        List<JSSourceFile> sourceFiles = new ArrayList<>();
        sourceFiles.add(inputSourceFile);
        compiler.compile(externs, sourceFiles, options);
        RenameLabels renameLabels = new RenameLabels(compiler);

        // Act
        renameLabels.process(compiler.getRoot(), compiler);

        // Assert
        Result result = compiler.getResult();
        assertFalse(result.errors.length > 0);
        assertEquals(expectedOutput, compiler.toSource());
    }

    @Test
    public void testProcess_withReferencedLabelInBreakOrContinue_renamesTheLabel() {
        // Arrange
        String input = "function foo() { a: { b: break a; } }";
        String expectedOutput = "function foo() { a1: { b: break a1; } }";
        JSSourceFile inputSourceFile = JSSourceFile.fromCode("input.js", input);
        List<JSSourceFile> sourceFiles = new ArrayList<>();
        sourceFiles.add(inputSourceFile);
        compiler.compile(externs, sourceFiles, options);
        RenameLabels renameLabels = new RenameLabels(compiler);

        // Act
        renameLabels.process(compiler.getRoot(), compiler);

        // Assert
        Result result = compiler.getResult();
        assertFalse(result.errors.length > 0);
        assertEquals(expectedOutput, compiler.toSource());
    }

    @Test
    public void testProcess_withInvalidLabelName_throwsException() {
        // Arrange
        String input = "function foo() { a,b,c: { } }";
        JSSourceFile inputSourceFile = JSSourceFile.fromCode("input.js", input);
        List<JSSourceFile> sourceFiles = new ArrayList<>();
        sourceFiles.add(inputSourceFile);
        compiler.compile(externs, sourceFiles, options);
        RenameLabels renameLabels = new RenameLabels(compiler);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> renameLabels.process(compiler.getRoot(), compiler));
    }

    @Test
    public void testProcess_withMultipleLabelsWithSameNameAcrossScopes_doesNotRenameTheLabels() {
        // Arrange
        String input = "function foo() { a: { } a: { } }";
        String expectedOutput = "function foo() { a: { } a: { } }";
        JSSourceFile inputSourceFile = JSSourceFile.fromCode("input.js", input);
        List<JSSourceFile> sourceFiles = new ArrayList<>();
        sourceFiles.add(inputSourceFile);
        compiler.compile(externs, sourceFiles, options);
        RenameLabels renameLabels = new RenameLabels(compiler);

        // Act
        renameLabels.process(compiler.getRoot(), compiler);

        // Assert
        Result result = compiler.getResult();
        assertFalse(result.errors.length > 0);
        assertEquals(expectedOutput, compiler.toSource());
    }

}