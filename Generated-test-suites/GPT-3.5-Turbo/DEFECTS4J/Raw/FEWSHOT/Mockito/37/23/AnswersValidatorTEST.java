import static org.junit.Assert.*;
import org.junit.Test;

public class AnswersValidatorTest {
    @Test
    public void testValidate_ThrowsException() {
        ThrowsException answer = new ThrowsException(new RuntimeException());
        Invocation invocation = new Invocation();
        AnswersValidator validator = new AnswersValidator();
        
        validator.validate(answer, invocation);
        
        // assert that no exception is thrown
    }
    
    @Test
    public void testValidate_Returns() {
        Returns answer = new Returns("result");
        Invocation invocation = new Invocation();
        AnswersValidator validator = new AnswersValidator();
        
        validator.validate(answer, invocation);
        
        // assert that no exception is thrown
    }
    
    @Test
    public void testValidate_DoNothing_ValidVoidMethod() {
        DoesNothing answer = new DoesNothing();
        Invocation invocation = new Invocation();
        invocation.setVoid(true);
        AnswersValidator validator = new AnswersValidator();
        
        validator.validate(answer, invocation);
        
        // assert that no exception is thrown
    }
    
    @Test
    public void testValidate_DoNothing_InvalidNonVoidMethod() {
        DoesNothing answer = new DoesNothing();
        Invocation invocation = new Invocation();
        invocation.setVoid(false);
        AnswersValidator validator = new AnswersValidator();
        
        try {
            validator.validate(answer, invocation);
            fail("Expected Reporter.onlyVoidMethodsCanBeSetToDoNothing() to be called");
        } catch (Exception e) {
            // assert that Reporter.onlyVoidMethodsCanBeSetToDoNothing() is called
        }
    }
}