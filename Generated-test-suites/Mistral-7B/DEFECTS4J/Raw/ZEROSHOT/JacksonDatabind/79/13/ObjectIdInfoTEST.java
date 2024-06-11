import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerators.HashCodeArrayIdGenerator.class, false, ObjectIdResolvers.StdObjectIdResolver.class);

 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerators$HashCodeArrayIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoDefaultConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerators.HashCodeArrayIdGenerator.class);

 assertEquals("ObjectIdInfo: propName=testPropertyName, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerators$HashCodeArrayIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoNoResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerators.HashCodeArrayIdGenerator.class, false);

 assertNull(objectIdInfo.getResolverType());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsIdTrue() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerators.HashCodeArrayIdGenerator.class, true, ObjectIdResolvers.StdObjectIdResolver.class);

 assertTrue(objectIdInfo.getAlwaysAsId());

 ObjectIdInfo objectIdInfoWithAlwaysAsIdFalse = new ObjectIdInfo(objectIdInfo.getPropertyName(), objectIdInfo.getScope(), objectIdInfo.getGeneratorType(), false, objectIdInfo.getResolverType());

 assertFalse(objectIdInfoWithAlwaysAsIdFalse.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsIdFalse() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerators.HashCodeArrayIdGenerator.class, false, ObjectIdResolvers.StdObjectIdResolver.class);

 assertFalse(objectIdInfo.getAlwaysAsId());

 ObjectIdInfo objectIdInfoWithAlwaysAsIdTrue = new ObjectIdInfo(objectIdInfo.getPropertyName(), objectIdInfo.getScope(), objectIdInfo.getGeneratorType(), true, objectIdInfo.getResolverType());

 assertTrue(objectIdInfoWithAlwaysAsIdTrue.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithNullResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testPropertyName"), Object.class, ObjectIdGenerators.HashCodeArrayIdGenerator.class, false, null);

 assertNotNull(objectIdInfo.getResolverType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
 }
}