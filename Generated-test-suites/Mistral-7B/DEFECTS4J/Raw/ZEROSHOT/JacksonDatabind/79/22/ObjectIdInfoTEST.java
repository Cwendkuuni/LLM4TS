import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class ObjectIdInfoTest {

 private static final PropertyName PROPERTY_NAME = new PropertyName("testProperty");
 private static final Class<ObjectIdInfo> CLASS = ObjectIdInfo.class;

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerator.class, false);
 assertNotNull(objectIdInfo);
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(false, objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructionWithDefaultResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerator.class);
 assertNotNull(objectIdInfo);
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(false, objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoConstructionWithCustomResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerator.class, false, MyObjectIdResolver.class);
 assertNotNull(objectIdInfo);
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(false, objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerator.class, true);
 assertNotNull(objectIdInfo);
 assertEquals(PROPERTY_NAME, objectIdInfo.getPropertyName());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
 assertEquals(true, objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsIdSetter() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerator.class, false);
 ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
 assertNotNull(newObjectIdInfo);
 assertEquals(PROPERTY_NAME, newObjectIdInfo.getPropertyName());
 assertEquals(Object.class, newObjectIdInfo.getScope());
 assertEquals(ObjectIdGenerator.class, newObjectIdInfo.getGeneratorType());
 assertEquals(true, newObjectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testToString() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerator.class, false);
 String toString = objectIdInfo.toString();
 String expected = "ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
 assertEquals(expected,toString);
 }

 @Test
 public void testEqualsAndHashcode() {
 ObjectIdInfo objectIdInfo1 = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerator.class, false);
 ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerator.class, false);
 ObjectIdInfo objectIdInfo3 = new ObjectIdInfo(PROPERTY_NAME, Object.class, ObjectIdGenerator.class, true);

 assertEquals(objectIdInfo1, objectIdInfo1);
 assertEquals(objectIdInfo1, objectIdInfo2);
 assertNotEquals(objectIdInfo1, objectIdInfo3);
 }

 @Test
 public void testMockitoMocks() {
 Class<?> mockGenerator = Mockito.mock(ObjectIdGenerator.class);
 Class<? extends ObjectIdResolver> mockResolver = Mockito.mock(ObjectIdResolver.class);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(PROPERTY_NAME, Object.class, mockGenerator, false);

 Mockito.verify(mockGenerator).getClass();
 Mockito.verifyNoMoreInteractions(mockGenerator);
 Mockito.verify(mockResolver).getClass();
 Mockito.verifyNoMoreInteractions(mockResolver);
 }
}