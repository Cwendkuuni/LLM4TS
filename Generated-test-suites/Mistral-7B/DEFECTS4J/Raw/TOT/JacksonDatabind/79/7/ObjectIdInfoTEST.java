import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SimpleObjectIdGenerator;
import com.fasterxml.jackson.databind.SimpleObjectIdResolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class, false);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class, false);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.ObjectIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, Object.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class, false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), null, ObjectIdGenerator.class, SimpleObjectIdResolver.class, false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullGeneratorType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null, SimpleObjectIdResolver.class, false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullResolverType() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, null);
 }

 @Test
 public void testGetters() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class, false);
 assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());
 }
}