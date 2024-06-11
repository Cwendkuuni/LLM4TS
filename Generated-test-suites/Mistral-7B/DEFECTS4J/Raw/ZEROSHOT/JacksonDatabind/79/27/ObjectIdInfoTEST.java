import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfo() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Object.class, Introspector.getAnnotation(Object.class, JsonIdentityInfo.class));

 assertEquals("name", objectIdInfo.getPropertyName().getId());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertNotNull(objectIdInfo.getGeneratorType());
 assertNotNull(objectIdInfo.getResolverType());
 assertFalse(objectIdInfo.getAlwaysAsId());

 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
 assertNotSame(objectIdInfo, objectIdInfoWithAlwaysAsId);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, Object.class, Introspector.getAnnotation(Object.class, JsonIdentityInfo.class));
 }

 @Test(expected = NullPointerException.class)
 public void testNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), null, Introspector.getAnnotation(Object.class, JsonIdentityInfo.class));
 }

 @Test(expected = NullPointerException.class)
 public void testNullAnnotation() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Object.class, null);
 }

 @Test
 public void testObjectIdInfoWithCustomResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), Object.class, Introspector.getAnnotation(Object.class, JsonIdentityInfo.class), MyCustomObjectIdResolver.class);

 assertEquals(MyCustomObjectIdResolver.class, objectIdInfo.getResolverType());
 }
}