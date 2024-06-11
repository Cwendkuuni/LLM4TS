import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.JavaType;

public class ObjectIdInfoTest {

 private static final PropertyName PROPERTY_NAME = new PropertyName("name");
 private static final Class<?> SCOPE = String.class;
 private static final Class<? extends ObjectIdGenerator<?>> GENERATOR = HashIdGenerator.class;
 private static final Class<? extends ObjectIdResolver> RESOLVER = SimpleObjectIdResolver.class;

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER);
 ObjectIdInfo expectedObjectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER, null);
 assertEquals(expectedObjectIdInfo, objectIdInfo);
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER);
 ObjectIdInfo alwaysAsIdObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
 ObjectIdInfo expectedAlwaysAsIdObjectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, true, RESOLVER);
 assertEquals(expectedAlwaysAsIdObjectIdInfo, alwaysAsIdObjectIdInfo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoNullResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER);
 ObjectIdInfo nullResolverObjectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER, null);
 ObjectIdInfo expectedNullResolverObjectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER, null);
 assertNotEquals(objectIdInfo, nullResolverObjectIdInfo);

 ObjectIdInfo nullResolverWithAlwaysAsIdObjectIdInfo = nullResolverObjectIdInfo.withAlwaysAsId(true);
 ObjectIdInfo expectedNullResolverWithAlwaysAsIdObjectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, true, null);
 assertNotEquals(nullResolverObjectIdInfo, nullResolverWithAlwaysAsIdObjectIdInfo);
 }

 @Test
 public void testToString() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER);
 String expectedToString = "ObjectIdInfo: propName=name, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.ser.std.HashIdGenerator, alwaysAsId=false";
 String toString = objectIdInfo.toString();
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testGetters() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER);
 PropertyName propertyName = objectIdInfo.getPropertyName();
 assertEquals(PROPERTY_NAME, propertyName);

 Class<?> scope = objectIdInfo.getScope();
 assertEquals(SCOPE, scope);

 Class<? extends ObjectIdGenerator<?>> generatorType = objectIdInfo.getGeneratorType();
 assertEquals(GENERATOR, generatorType);

 Class<? extends ObjectIdResolver> resolverType = objectIdInfo.getResolverType();
 assertEquals(RESOLVER, resolverType);

 boolean alwaysAsId = objectIdInfo.getAlwaysAsId();
 assertFalse(alwaysAsId);
 }

 @Test
 public void testGettersWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, SCOPE, GENERATOR, RESOLVER);
 ObjectIdInfo alwaysAsIdObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

 PropertyName propertyName = alwaysAsIdObjectIdInfo.getPropertyName();
 assertEquals(PROPERTY_NAME, propertyName);

 Class<?> scope = alwaysAsIdObjectIdInfo.getScope();
 assertEquals(SCOPE, scope);

 Class<? extends ObjectIdGenerator<?>> generatorType = alwaysAsIdObjectIdInfo.getGeneratorType();
 assertEquals(GENERATOR, generatorType);

 Class<? extends ObjectIdResolver> resolverType = alwaysAsIdObjectIdInfo.getResolverType();
 assertEquals(RESOLVER, resolverType);

 boolean alwaysAsId = alwaysAsIdObjectIdInfo.getAlwaysAsId();
 assertTrue(alwaysAsId);
 }
}