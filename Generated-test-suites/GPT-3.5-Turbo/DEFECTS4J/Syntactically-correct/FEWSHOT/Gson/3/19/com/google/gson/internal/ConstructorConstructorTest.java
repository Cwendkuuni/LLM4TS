import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {
    @Test
    public void testGet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        TypeToken<String> stringToken = TypeToken.get(String.class);
        ObjectConstructor<String> stringConstructor = constructorConstructor.get(stringToken);
        assertNull(stringConstructor);

        TypeToken<Integer> integerToken = TypeToken.get(Integer.class);
        ObjectConstructor<Integer> integerConstructor = constructorConstructor.get(integerToken);
        assertNull(integerConstructor);
    }

    @Test
    public void testNewDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());

        ObjectConstructor<String> stringConstructor = constructorConstructor.newDefaultConstructor(String.class);
        assertNotNull(stringConstructor);

        ObjectConstructor<Integer> integerConstructor = constructorConstructor.newDefaultConstructor(Integer.class);
        assertNotNull(integerConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());

        TypeToken<String> stringToken = TypeToken.get(String.class);
        ObjectConstructor<String> stringConstructor = constructorConstructor.newDefaultImplementationConstructor(stringToken.getType(), stringToken.getRawType());
        assertNull(stringConstructor);

        TypeToken<Map<String, Object>> mapToken = new TypeToken<Map<String, Object>>() {};
        ObjectConstructor<Map<String, Object>> mapConstructor = constructorConstructor.newDefaultImplementationConstructor(mapToken.getType(), mapToken.getRawType());
        assertNotNull(mapConstructor);
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());

        TypeToken<String> stringToken = TypeToken.get(String.class);
        ObjectConstructor<String> stringConstructor = constructorConstructor.newUnsafeAllocator(stringToken.getType(), stringToken.getRawType());
        assertNotNull(stringConstructor);

        TypeToken<Integer> integerToken = TypeToken.get(Integer.class);
        ObjectConstructor<Integer> integerConstructor = constructorConstructor.newUnsafeAllocator(integerToken.getType(), integerToken.getRawType());
        assertNotNull(integerConstructor);
    }

    @Test
    public void testToString() {
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<Type, InstanceCreator<?>>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

        String result = constructorConstructor.toString();
        assertNotNull(result);
    }
}