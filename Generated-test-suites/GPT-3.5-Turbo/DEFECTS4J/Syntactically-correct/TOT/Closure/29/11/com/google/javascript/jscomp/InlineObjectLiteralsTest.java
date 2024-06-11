import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InlineObjectLiteralsTest {

    private InlineObjectLiterals inlineObjectLiterals;

    @Before
    public void setUp() {
        inlineObjectLiterals = new InlineObjectLiterals();
    }

    @Test
    public void testProcess_typicalUseCase() {
        // Arrange
        String input = "var x = {a: 1, b: 2};";
        
        // Act
        String output = inlineObjectLiterals.process(input);
        
        // Assert
        assertEquals("var x = {a: 1, b: 2, true};", output);
    }

    @Test
    public void testProcess_edgeCaseEmptyObjectLiteral() {
        // Arrange
        String input = "var x = {};";

        // Act
        String output = inlineObjectLiterals.process(input);

        // Assert
        assertEquals("var x = {true};", output);
    }

    @Test
    public void testProcess_edgeCaseUndefinedAssignment() {
        // Arrange
        String input = "var x = {a: 1, b: undefined};";

        // Act
        String output = inlineObjectLiterals.process(input);

        // Assert
        assertEquals("var x = {a: 1, b: undefined, true};", output);
    }

    @Test
    public void testProcess_errorCaseSelfReferentialAssignment() {
        // Arrange
        String input = "var x = {a: 1, b: x.a};";

        // Act
        String output = inlineObjectLiterals.process(input);

        // Assert
        assertEquals("var x = {a: 1, b: x.a};", output);
    }

    @Test
    public void testProcess_errorCaseES5GetterSetter() {
        // Arrange
        String input = "var x = {get a() { return 1; }};";

        // Act
        String output = inlineObjectLiterals.process(input);

        // Assert
        assertEquals("var x = {get a() { return 1; }, true};", output);
    }
}