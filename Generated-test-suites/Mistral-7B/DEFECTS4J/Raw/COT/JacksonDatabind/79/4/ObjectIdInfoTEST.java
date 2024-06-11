import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

 private ObjectIdInfo objectIdInfo;

 @Test
 public void testObjectIdInfoWithAllArguments() {
 objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false, ObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithThreeArguments() {
 objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithTwoArguments() {
 objectIdInfo = new ObjectIdInfo("testProperty", Object.class, ObjectIdGenerator.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithDeprecatedConstructors() {
 objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullPropertyName() {
 assertNull(new ObjectIdInfo(null, Object.class, ObjectIdGenerator.class, false, ObjectIdResolver.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullScope() {
 assertNull(new ObjectIdInfo(new PropertyName("testProperty"), null, ObjectIdGenerator.class, false, ObjectIdResolver.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullGeneratorType() {
 assertNull(new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null, false, ObjectIdResolver.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullResolverType() {
 assertNull(new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false, null));
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfoWithAlwaysAsId = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, true, ObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
 }
}