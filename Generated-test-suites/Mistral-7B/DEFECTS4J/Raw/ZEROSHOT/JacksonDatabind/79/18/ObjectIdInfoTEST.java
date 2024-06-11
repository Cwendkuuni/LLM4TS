import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoCreation() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class,
 JsonIdentityInfo.None.class, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.JsonIdentityInfo$None, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoCreationWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class,
 JsonIdentityInfo.None.class, true, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.JsonIdentityInfo$None, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoCreationWithGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class,
 ObjectIdGenerator.PropertyGenerator.class, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator$PropertyGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoCreationWithCustomResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class,
 JsonIdentityInfo.None.class, false, MyObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.JsonIdentityInfo$None, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class,
 JsonIdentityInfo.None.class, false, SimpleObjectIdResolver.class);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.JsonIdentityInfo$None, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), null,
 JsonIdentityInfo.None.class, false, SimpleObjectIdResolver.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, Object.class,
 JsonIdentityInfo.None.class, false, SimpleObjectIdResolver.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null,
 false, SimpleObjectIdResolver.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullPropertyNameAndGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, Object.class, null,
 false, SimpleObjectIdResolver.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class,
 JsonIdentityInfo.None.class, false, null);
 }

}