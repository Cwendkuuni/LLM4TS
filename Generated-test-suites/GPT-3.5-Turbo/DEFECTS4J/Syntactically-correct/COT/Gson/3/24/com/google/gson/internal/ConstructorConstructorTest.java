
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

    private ConstructorConstructor constructorConstructor;

    @BeforeEach
    public void setup() {
        constructorConstructor = new ConstructorConstructor(null);
    }

    @Test
    public void testGetWithTypeCreator() {
        TypeToken<String> typeToken = TypeToken.get(String.class);

        ObjectConstructor<String> result = constructorConstructor.get(typeToken);

        Assertions.assertNotNull(result);
    }

    @Test
    public void testGetWithRawTypeCreator() {
        TypeToken<Integer> typeToken = TypeToken.get(Integer.class);

        ObjectConstructor<Integer> result = constructorConstructor.get(typeToken);

        Assertions.assertNotNull(result);
    }

    @Test
    public void testGetWithDefaultConstructor() {
        TypeToken<ConstructorConstructor> typeToken = TypeToken.get(ConstructorConstructor.class);

        ObjectConstructor<ConstructorConstructor> result = constructorConstructor.get(typeToken);

        Assertions.assertNotNull(result);
    }

    @Test
    public void testGetWithDefaultImplementationConstructor() {
        TypeToken<LinkedHashMap<String, Integer>> typeToken = new TypeToken<LinkedHashMap<String, Integer>>() {};

        ObjectConstructor<LinkedHashMap<String, Integer>> result = constructorConstructor.get(typeToken);

        Assertions.assertNotNull(result);
    }

    @Test
    public void testGetWithUnsafeAllocator() {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);

        ObjectConstructor<Object> result = constructorConstructor.get(typeToken);

        Assertions.assertNotNull(result);
    }
}
