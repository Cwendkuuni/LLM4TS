import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorConstructorTest {

   private ConstructorConstructor constructorConstructor;

   @Test
   public void testGet() {
      constructorConstructor = new ConstructorConstructor(null);
      TypeToken<String> typeToken = TypeToken.get(String.class);
      ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
      assertNotNull(objectConstructor);
   }

   @Test
   public void testNewDefaultConstructor() {
      constructorConstructor = new ConstructorConstructor(null);
      Class<String> rawType = String.class;
      ObjectConstructor<String> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);
      assertNotNull(objectConstructor);
   }

   @Test
   public void testNewDefaultImplementationConstructor() {
      constructorConstructor = new ConstructorConstructor(null);
      TypeToken<Map<String, Integer>> typeToken = new TypeToken<Map<String, Integer>>() {};
      ObjectConstructor<Map<String, Integer>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(typeToken.getType(), Map.class);
      assertNotNull(objectConstructor);
   }

   @Test
   public void testNewUnsafeAllocator() {
      constructorConstructor = new ConstructorConstructor(null);
      TypeToken<String> typeToken = TypeToken.get(String.class);
      ObjectConstructor<String> objectConstructor = constructorConstructor.newUnsafeAllocator(typeToken.getType(), String.class);
      assertNotNull(objectConstructor);
   }

   @Test
   public void testToString() {
      constructorConstructor = new ConstructorConstructor(null);
      String toString = constructorConstructor.toString();
      assertNotNull(toString);
   }

}