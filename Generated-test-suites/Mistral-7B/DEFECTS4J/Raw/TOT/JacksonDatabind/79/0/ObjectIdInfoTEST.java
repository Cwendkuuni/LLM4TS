import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), String.class, JsonIdentityInfo.None.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.annotation.JsonIdentityInfo$None, alwaysAsId=false", objIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), String.class, JsonIdentityInfo.None.class);
 ObjectIdInfo objIdInfoWithalwaysAsId = objIdInfo.withAlwaysAsId(true);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.annotation.JsonIdentityInfo$None, alwaysAsId=true", objIdInfoWithalwaysAsId.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullPropertyName() {
 PropertyName propName = null;
 try {
 new ObjectIdInfo(propName, String.class, JsonIdentityInfo.None.class);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullGeneratorType() {
 Class<? extends ObjectIdGenerator<?>> generatorType = null;
 try {
 new ObjectIdInfo(new PropertyName("testProperty"), String.class, generatorType);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullResolverType() {
 Class<? extends ObjectIdResolver> resolverType = null;
 try {
 new ObjectIdInfo(new PropertyName("testProperty"), String.class, JsonIdentityInfo.None.class, resolverType);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}