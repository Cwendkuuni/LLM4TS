import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private final SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_ValidType() throws JsonMappingException {
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 DeserializationContext context = new DeserializationContext(new AnonymousClass_(), null, null);
 validator.validateSubType(context, validType);
 // No exception should be thrown
 }

 @Test
 public void testValidateSubType_InvalidType() throws JsonMappingException {
 JavaType invalidType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
 DeserializationContext context = new DeserializationContext(new AnonymousClass_(), null, null);
 try {
 validator.validateSubType(context, invalidType);
 Fail("This method should have thrown an exception");
 } catch (JsonMappingException e) {
 String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", SubTypeValidator.class.getName());
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.startsWith(expectedMessage));
 }
 }

 @Test
 public void testValidateSubType_PrefixString_ValidSubClasses() throws JsonMappingException {
 JavaType prefixStringType = TypeFactory.defaultInstance().constructType(AbstractApplicationContext.class);
 DeserializationContext context = new DeserializationContext(new AnonymousClass_(), null, null);
 validator.validateSubType(context, prefixStringType);
 // No exception should be thrown
 }

 @Test
 public void testValidateSubType_PrefixString_InvalidSubClass() throws JsonMappingException {
 JavaType prefixStringInvalidType = TypeFactory.defaultInstance().constructType(AbstractPointcutAdvisor.class);
 DeserializationContext context = new DeserializationContext(new AnonymousClass_(), null, null);
 try {
 validator.validateSubType(context, prefixStringInvalidType);
 Fail("This method should have thrown an exception");
 } catch (JsonMappingException e) {
 String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", AbstractPointcutAdvisor.class.getName());
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.startsWith(expectedMessage));
 }
 }

 @Test
 public void testValidateSubType_EmptyString() throws JsonMappingException {
 JavaType emptyStringType = TypeFactory.defaultInstance().constructType(String.class);
 DeserializationContext context = new DeserializationContext(new AnonymousClass_(), null, null);
 validator.validateSubType(context, ClassIdType.construct(emptyStringType.getId()));
 // No exception should be thrown
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalClassName() throws JsonMappingException {
 JavaType illegalClassNameType = TypeFactory.defaultInstance().constructType(String.class);
 String illegalClassName = "java.lang.Runtime";
 illegalClassNameType = TypeFactory.defaultInstance()
 .constructType(ClassIdType.construct(illegalClassNameType.getId().getId(), illegalClassName));
 DeserializationContext context = new DeserializationContext(new AnonymousClass_(), null, null);
 validator.validateSubType(context, illegalClassNameType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_NullType() throws JsonMappingException {
 DeserializationContext context = new DeserializationContext(new AnonymousClass_(), null, null);
 validator.validateSubType(context, null);
 }

 private static class AnonymousClass_ {
 }

}