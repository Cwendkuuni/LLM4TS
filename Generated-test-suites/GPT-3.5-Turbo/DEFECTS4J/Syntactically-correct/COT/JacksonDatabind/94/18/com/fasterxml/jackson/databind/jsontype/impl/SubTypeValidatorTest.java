
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        SubTypeValidator instance = SubTypeValidator.instance();

        try {
            instance.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

}
