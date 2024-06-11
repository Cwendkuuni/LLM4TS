import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_NullType() {
  try {
   validator.validateSubType(null, null);
  } catch (JsonMappingException e) {
   assertEquals("Cannot deserialize value of type null", e.getMessage());
  }
 }

 @Test
 public void testValidateSubType_ValidType() {
  JavaType type = new JavaType(String.class);
  validator.validateSubType(new DeserializationContext(null), type);
 }

 @Test
 public void testValidateSubType_NastyClass() {
  JavaType nastyType = new JavaType(SpringBeanFactoryPointcutAdvisor.class);
  String expectedMessage = "Illegal type (org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor) to deserialize: prevented for security reasons";

  try {
   validator.validateSubType(new DeserializationContext(null), nastyType);
   fail("Should have thrown JsonMappingException");
  } catch (JsonMappingException e) {
   assertEquals(expectedMessage, e.getMessage());
  }
 }

 @Test
 public void testValidateSubType_SpringClassWithSubclassNastyClass() {
  JavaType springType = new JavaType(AbstractApplicationContext.class);

  try {
   validator.validateSubType(new DeserializationContext(null), springType);
  } catch (JsonMappingException e) {
   fail("Should not have thrown JsonMappingException for Spring class");
  }

  JavaType nastySubclassType = new JavaType(AbstractPointcutAdvisor.class);

  try {
   validator.validateSubType(new DeserializationContext(null), nastySubclassType);
   fail("Should have thrown JsonMappingException");
  } catch (JsonMappingException e) {
   // Expected
  }
 }

 @Test
 public void testValidateSubType_UnknownClassPrefix() {
  JavaType unknownPrefixType = new JavaType(UnknownPrefixClass.class);

  try {
   validator.validateSubType(new DeserializationContext(null), unknownPrefixType);
   fail("Should have thrown JsonMappingException");
  } catch (JsonMappingException e) {
   // Expected
  }
 }

}

class UnknownPrefixClass {
 // No need to implement anything here
}