import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_ValidType() throws JsonMappingException {
 JavaType validType = JavaType.construct(String.class);
 validator.validateSubType(new DeserializationContext(null), validType);
 }

 @Test
 public void testValidateSubType_NastyClassName() throws JsonMappingException {
 JavaType nastyType = JavaType.construct(com.apache.commons.collections4.functors.InstantiateTransformer.class);
 try {
 validator.validateSubType(new DeserializationContext(null), nastyType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubType_SpringApplicationContext() throws JsonMappingException {
 JavaType applicationContextType = JavaType.construct(com.fasterxml.jackson.core.type.TypeReference.unconstructedType(Object.class, "AbstractApplicationContext"));
 try {
 validator.validateSubType(new DeserializationContext(null), applicationContextType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubType_ValidTypeWithPrefix() throws JsonMappingException {
 JavaType validTypeWithPrefix = JavaType.construct(com.fasterxml.jackson.core.type.TypeReference.unconstructedType(String.class, "org.springframework.mypackage."));
 validator.validateSubType(new DeserializationContext(null), validTypeWithPrefix);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalTypeWithPrefix() throws JsonMappingException {
 JavaType illegalTypeWithPrefix = JavaType.construct(com.fasterxml.jackson.core.type.TypeReference.unconstructedType(String.class, "org.springframework.illegalpackage."));
 validator.validateSubType(new DeserializationContext(null), illegalTypeWithPrefix);
 }

}