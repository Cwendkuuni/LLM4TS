import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class,
 JsonIdentityInfo.None.class, false, SimpleObjectIdResolver.class);
 assertNotNull(objectIdInfo);
 assertEquals("id", objectIdInfo.getPropertyName().getId());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(JsonIdentityInfo.None.class, objectIdInfo.getGeneratorType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
 assertEquals(false, objectIdInfo.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), Object.class,
 JsonIdentityInfo.None.class, false, SimpleObjectIdResolver.class);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertNotNull(objectIdInfoWithAlwaysAsId);
 assertEquals(objectIdInfo.getPropertyName(), objectIdInfoWithAlwaysAsId.getPropertyName());
 assertEquals(objectIdInfo.getScope(), objectIdInfoWithAlwaysAsId.getScope());
 assertEquals(objectIdInfo.getGeneratorType(), objectIdInfoWithAlwaysAsId.getGeneratorType());
 assertEquals(objectIdInfo.getResolverType(), objectIdInfoWithAlwaysAsId.getResolverType());
 assertEquals(true, objectIdInfoWithAlwaysAsId.getAlwaysAsId());
 }

 @Test
 public void testObjectIdInfoDeprecatedConstructions() {
 PropertyName propertyName = new PropertyName("id");
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, Object.class, JsonIdentityInfo.None.class);
 assertNotNull(objectIdInfo);
 assertEquals("id", objectIdInfo.getPropertyName().getId());
 assertEquals(Object.class, objectIdInfo.getScope());
 assertEquals(JsonIdentityInfo.None.class, objectIdInfo.getGeneratorType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
 assertEquals(false, objectIdInfo.getAlwaysAsId());

 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertNotNull(objectIdInfoWithAlwaysAsId);
 assertEquals(objectIdInfo.getPropertyName(), objectIdInfoWithAlwaysAsId.getPropertyName());
 assertEquals(objectIdInfo.getScope(), objectIdInfoWithAlwaysAsId.getScope());
 assertEquals(objectIdInfo.getGeneratorType(), objectIdInfoWithAlwaysAsId.getGeneratorType());
 assertEquals(objectIdInfo.getResolverType(), objectIdInfoWithAlwaysAsId.getResolverType());
 assertEquals(true, objectIdInfoWithAlwaysAsId.getAlwaysAsId());

 ObjectIdInfo objectIdInfoWithNameAndScope = new ObjectIdInfo("id", Object.class, JsonIdentityInfo.None.class, false);
 assertNotNull(objectIdInfoWithNameAndScope);
 assertEquals("id", objectIdInfoWithNameAndScope.getPropertyName().getId());
 assertEquals(Object.class, objectIdInfoWithNameAndScope.getScope());
 assertEquals(JsonIdentityInfo.None.class, objectIdInfoWithNameAndScope.getGeneratorType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfoWithNameAndScope.getResolverType());
 assertEquals(false, objectIdInfoWithNameAndScope.getAlwaysAsId());

 ObjectIdInfo objectIdInfoWithNameAndScopeAndGenerator = new ObjectIdInfo("id", Object.class, JsonIdentityInfo.None.class, false, SimpleObjectIdResolver.class);
 assertNotNull(objectIdInfoWithNameAndScopeAndGenerator);
 assertEquals("id", objectIdInfoWithNameAndScopeAndGenerator.getPropertyName().getId());
 assertEquals(Object.class, objectIdInfoWithNameAndScopeAndGenerator.getScope());
 assertEquals(JsonIdentityInfo.None.class, objectIdInfoWithNameAndScopeAndGenerator.getGeneratorType());
 assertEquals(SimpleObjectIdResolver.class, objectIdInfoWithNameAndScopeAndGenerator.getResolverType());
 assertEquals(false, objectIdInfoWithNameAndScopeAndGenerator.getAlwaysAsId());
 }

}