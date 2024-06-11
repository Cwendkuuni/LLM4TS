import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

 private final SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_ValidType() throws JsonMappingException {
 JavaType validType = new ClassType(Integer.class);
 validator.validateSubType(new DeserializationContext(null), validType);
 }

 @Test
 public void testValidateSubType_IllegalType_ContainsInSet() throws JsonMappingException {
 String illegalTypeName = "org.apache.commons.collections.functors.InvokerTransformer";
 JavaType illegalType = new ClassType(Class.forName(illegalTypeName));
 try {
 validator.validateSubType(new DeserializationContext(null), illegalType);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected exception, so assert message is as expected
 String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", illegalTypeName);
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test
 public void testValidateSubType_IllegalType_StartsWithPrefix() throws JsonMappingException {
 String illegalTypeName = "org.springframework.beans.factory.ObjectFactory";
 JavaType illegalType = new ClassType(Class.forName(illegalTypeName));
 try {
 validator.validateSubType(new DeserializationContext(null), illegalType);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected exception, so assert message is as expected
 String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", illegalTypeName);
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalType_ThrowsException() throws JsonMappingException {
 String illegalTypeName = "java.util.logging.FileHandler";
 JavaType illegalType = new ClassType(Class.forName(illegalTypeName));
 validator.validateSubType(new DeserializationContext(null), illegalType);
 }

 @Test
 public void testInstance() {
 SubTypeValidator validator = SubTypeValidator.instance();
 assertSame(validator, SubTypeValidator.instance());
 }

}