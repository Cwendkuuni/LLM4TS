import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {
   @Test
   public void testToString() {
      Class<?> collectionClass = ArrayList.class;
      TypeBindings bindings = new TypeBindings();
      JavaType elemType = IntegerType.INSTANCE;
      CollectionType collectionType = CollectionType.construct(collectionClass, bindings, null, null, elemType);
      String expected = "[collection type; class " + collectionClass.getName() + ", contains " + elemType + "]";
      String actual = collectionType.toString();
      assertEquals(expected, actual);
   }
}