
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

   @Test
   public void testValidateSubType_ValidType() {
       SubTypeValidator validator = SubTypeValidator.instance();
       DeserializationContext ctxt = new DeserializationContext(null, null);
       JavaType type = new JavaType();
       
       try {
           validator.validateSubType(ctxt, type);
       } catch (JsonMappingException e) {
           fail("Unexpected exception occurred");
       }
   }
   
   @Test(expected = JsonMappingException.class)
   public void testValidateSubType_InvalidType() throws JsonMappingException {
       SubTypeValidator validator = SubTypeValidator.instance();
       DeserializationContext ctxt = new DeserializationContext(null, null);
       JavaType type = new JavaType();
       
       validator.validateSubType(ctxt, type);
   }
}
