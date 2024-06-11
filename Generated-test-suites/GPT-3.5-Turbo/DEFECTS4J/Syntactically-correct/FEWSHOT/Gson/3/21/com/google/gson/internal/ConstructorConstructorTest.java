import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.LinkedTreeMap;
import sun.misc.Unsafe;
import java.lang.reflect.Field;

public class ConstructorConstructorTest {
    private static Unsafe unsafe;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
        ObjectConstructor<Integer> result = constructorConstructor.get(typeToken);
        assertNotNull(result);
    }

    @Test
    public void testNewDefaultConstructor() throws NoSuchMethodException {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        Class<String> rawType = String.class;
        ObjectConstructor<String> result = constructorConstructor.newDefaultConstructor(rawType);
        assertNull(result);
    }

    @Test
    public void testNewDefaultImplementationConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        Type type = new TypeToken<Collection<String>>() {}.getType();
        Class<Collection<String>> rawType = new TypeToken<Collection<String>>() {}.getRawType();
        ObjectConstructor<Collection<String>> result = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
        assertNotNull(result);
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        Type type = new TypeToken<Integer>() {}.getType();
        Class<Integer> rawType = new TypeToken<Integer>() {}.getRawType();
        ObjectConstructor<Integer> result = constructorConstructor.newUnsafeAllocator(type, rawType);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<Type, InstanceCreator<?>>());
        String result = constructorConstructor.toString();
        assertNotNull(result);
    }

}