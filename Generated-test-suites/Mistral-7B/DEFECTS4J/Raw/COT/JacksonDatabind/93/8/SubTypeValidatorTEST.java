import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SubTypeValidatorTest {

 private SubTypeValidator validator;
 private DeserializationContext ctx;
 private JavaType type;

 @Before
 public void setup() {
 validator = SubTypeValidator.instance();
 ctx = new DeserializationContext(new TestDeserializationConfig(), new TestObjectMapper());
 type = TypeFactory.defaultInstance()
 .constructType(String.class, new SimpleType(String.class));
 }

 @Test
 public void testValidateSubType_ValidType() throws JsonMappingException {
 validator.validateSubType(ctx, type);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalType() throws JsonMappingException {
 validator.validateSubType(ctx, getClassType("org.apache.commons.collections4.functors.InstantiateTransformer"));
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringClass() throws JsonMappingException {
 validator.validateSubType(ctx, getClassType("org.springframework.beans.factory.config.PropertyPathFactoryBean"));
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_NestedSpringClass() throws JsonMappingException {
 validator.validateSubType(ctx, getNestedSpringClassType("org.springframework.beans.factory.config.PropertyPathFactoryBean", "PropertyAccessorFactory"));
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_CustomIllegalType() throws JsonMappingException {
 Set<String> customIllegalTypes = new HashSet<>();
 customIllegalTypes.add("com.test.CustomIllegalType");
 SubTypeValidator customValidator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = customIllegalTypes;
 };
 customValidator.validateSubType(ctx, getClassType("com.test.CustomIllegalType"));
 }

 private JavaType getClassType(String className) {
 return TypeFactory.defaultInstance().constructType(className);
 }

 private JavaType getNestedType(String className, String nestedClassName) {
 JavaType superType = TypeFactory.defaultInstance().constructType(className);
 return TypeFactory.defaultInstance().constructType(superType.getRawClass(), nestedClassName);
 }

 private JavaType getNestedSpringClassType(String mainClassName, String nestedClassName) {
 JavaType superType = getClassType(mainClassName);
 for (Class<?> clazz = superType.getRawClass().getSuperclass(); clazz != null; clazz = clazz.getSuperclass()) {
 if (clazz.getSimpleName().equals("PropertyAccessorFactory")) {
 return TypeFactory.defaultInstance().constructType(superType, nestedClassName);
 }
 }
 return null;
 }

}