import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoWithValidArguments() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.Int.class);

 assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerators.Int.class, objectIdInfo.getGeneratorType());
 Assert.assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithDefaultArguments() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class);

 assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 Assert.assertNull(objectIdInfo.getGeneratorType());
 Assert.assertFalse(objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithStringArgumentForPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.Int.class);
 ObjectIdInfo objectIdInfoWithString = new ObjectIdInfo("testProperty", Object.class, ObjectIdGenerators.Int.class);

 assertEquals(objectIdInfo, objectIdInfoWithString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullPropertyName() {
 try {
 new ObjectIdInfo(null, Object.class, ObjectIdGenerators.Int.class);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullScope() {
 try {
 new ObjectIdInfo(new PropertyName("testProperty"), null, ObjectIdGenerators.Int.class);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullGenerator() {
 try {
 new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.Int.class);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

 assertEquals(objectIdInfo, objectIdInfoWithAlwaysAsId);
 assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithNullResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.Int.class, false, null);

 assertEquals(ObjectIdResolver.class, objectIdInfo.getResolverType());
 }

 @Test
 public void testObjectIdInfoToString() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerators.Int.class);

 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.json.ObjectIdGenerators$Int, alwaysAsId=false", objectIdInfo.toString());
 }
}