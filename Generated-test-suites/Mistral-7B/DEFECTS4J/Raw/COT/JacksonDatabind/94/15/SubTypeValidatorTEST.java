import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_ValidType() throws JsonMappingException {
 DeserializationContext context = null; // not used in this test
 JavaType validType = JavaType.fromClass(String.class);
 validator.validateSubType(context, validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_InvalidType_SpringClass() throws JsonMappingException {
 DeserializationContext context = null;
 JavaType invalidType = JavaType.fromClass(AbstractApplicationContext.class);
 validator.validateSubType(context, invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_InvalidType_SpringInterface() throws JsonMappingException {
 DeserializationContext context = null;
 JavaType invalidType = JavaType.fromClass(AbstractPointcutAdvisor.class);
 validator.validateSubType(context, invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_InvalidType_NastyClass() throws JsonMappingException {
 DeserializationContext context = null;
 JavaType invalidType = JavaType.fromClass(InvokerTransformer.class);
 validator.validateSubType(context, invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_InvalidType_EmptyString() throws JsonMappingException {
 DeserializationContext context = null;
 JavaType invalidType = JavaType.fromClass(String.class).withoutParameter(ClassIdType.CLASS_ID);
 validator.validateSubType(context, invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_InvalidType_NullType() throws JsonMappingException {
 DeserializationContext context = null;
 JavaType invalidType = null;
 validator.validateSubType(context, invalidType);
 }
}