import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.IdIntGenerator.class, false, ObjectIdResolver.StdObjectIdResolver.class);

 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$IdIntGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoDefaultConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.IdIntGenerator.class);

 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$IdIntGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoStringConstructor() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.IdIntGenerator.class);

 ObjectIdInfo objectIdInfoWithName = new ObjectIdInfo("testPropertyName", Object.class, ObjectIdGenerator.IdIntGenerator.class);

 assertEquals(objectIdInfo, objectIdInfoWithName);
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.IdIntGenerator.class, true, ObjectIdResolver.StdObjectIdResolver.class);

 ObjectIdInfo objectIdInfoWithAlwaysAsIdFalse = new ObjectIdInfo(objectIdInfo._propertyName, objectIdInfo._scope, objectIdInfo._generator, false, objectIdInfo._resolver);

 assertFalse(objectIdInfoWithAlwaysAsIdFalse.getAlwaysAsId());

 ObjectIdInfo objectIdInfoWithAlwaysAsIdTrue = new ObjectIdInfo(objectIdInfo._propertyName, objectIdInfo._scope, objectIdInfo._generator, true, objectIdInfo._resolver);

 assertTrue(objectIdInfoWithAlwaysAsIdTrue.getAlwaysAsId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullPropertyName() {
 new ObjectIdInfo(null, Object.class, ObjectIdGenerator.IdIntGenerator.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullScope() {
 new ObjectIdInfo(new PropertyName("testPropertyName"), null, ObjectIdGenerator.IdIntGenerator.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullGenerator() {
 new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullResolver() {
 new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerator.IdIntGenerator.class, false, null);
 }
}