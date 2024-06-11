import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

   @Test
   public void createTest() {
     ConstructorConstructor constructorConstructor = new ConstructorConstructor();
     JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
     Gson gson = new Gson();
     TypeToken<MyObject> targetType = TypeToken.get(MyObject.class);
     TypeAdapter<MyObject> adapter = factory.create(gson, targetType);
     assertNotNull("Adapter should not be null", adapter);
   }

   @Test
   public void getTypeAdapterWithTypeAdapterTest() {
     ConstructorConstructor constructorConstructor = new ConstructorConstructor();
     Gson gson = new Gson();
     TypeToken<MyObject> fieldType = TypeToken.get(MyObject.class);
     JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
     assertNotNull("Annotation should not be null", annotation);
     TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
     assertNotNull("Adapter should not be null", adapter);
   }

   @Test
   public void getTypeAdapterWithTypeAdapterFactoryTest() {
     ConstructorConstructor constructorConstructor = new ConstructorConstructor();
     Gson gson = new Gson();
     TypeToken<MyObject> fieldType = TypeToken.get(MyObject.class);
     JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
     assertNotNull("Annotation should not be null", annotation);
     TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
     assertNotNull("Adapter should not be null", adapter);
   }

   @Test(expected = IllegalArgumentException.class)
   public void getTypeAdapterWithInvalidValueTest() {
     ConstructorConstructor constructorConstructor = new ConstructorConstructor();
     Gson gson = new Gson();
     TypeToken<MyObject> fieldType = TypeToken.get(MyObject.class);
     JsonAdapter annotation = fieldType.getRawType().getAnnotation(JsonAdapter.class);
     assertNotNull("Annotation should not be null", annotation);
     annotation = new JsonAdapter() {
       @Override
       public Class<? extends TypeAdapter> value() {
         return null;
       }
       @Override
       public Class<?>[] classes() {
         return new Class<?>[0];
       }
       @Override
       public Class<?>[] types() {
         return new Class<?>[0];
       }
     };
     JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
   }
}