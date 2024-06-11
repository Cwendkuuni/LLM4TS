import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionLikeTypeTest {

 @Test
 public void testConstructFromClass() {
 Class<Collection<Integer>> collectionType = Collection.class;
 JavaType javaType = CollectionLikeType.construct(collectionType, TypeBindings.emptyBindings(),
 null, null, TypeFactory.defaultInstance.constructType(Integer.class));
 CollectionLikeType actual = (CollectionLikeType) javaType;
 JavaType expected = TypeFactory.defaultInstance.constructType(Collection.class, Integer.class);
 assertEquals(expected, actual.getRawType());
 JavaType elementType = actual.getElementType();
 assertEquals(Integer.class, elementType.getRawClass());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType javaType = TypeFactory.defaultInstance.constructType(ArrayList.class, Integer.class);
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, Integer.class);
 assertEquals(javaType.getClass(), collectionLikeType.getClass());
 CollectionLikeType actual = (CollectionLikeType) collectionLikeType;
 assertEquals(Integer.class, actual.getElementType().getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidType() {
 JavaType javaType = TypeFactory.defaultInstance.constructType(String.class);
 // JavaType elementType = TypeFactory.defaultInstance.constructType(Integer.class); // Uncomment this line for passing test
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, Integer.class);
 fail("Expected IllegalArgumentException");
 }
}