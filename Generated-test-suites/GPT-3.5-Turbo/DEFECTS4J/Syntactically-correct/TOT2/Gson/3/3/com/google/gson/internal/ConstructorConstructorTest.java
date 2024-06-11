import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import static org.junit.Assert.*;
import com.google.gson.internal.ConstructorConstructor;

public class ConstructorConstructorTest {

    @Test
    public void testConstructorConstructor() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        assertNotNull(constructorConstructor);
    }

    @Test
    public void testGet() {
        Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        TypeToken<String> typeToken = TypeToken.get(String.class);
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }

    @Test
    public void testNewDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        Class<?> rawType = String.class;
        ObjectConstructor<String> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);
        assertNotNull(objectConstructor);
    }

    @Test
    public void testNewDefaultImplementationConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        Class<?> rawType = ArrayList.class;
        ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);
        assertNotNull(objectConstructor);
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new LinkedHashMap<>());
        Type type = new TypeToken<LinkedList<String>>() {}.getType();
        Class<?> rawType = LinkedList.class;
        ObjectConstructor<LinkedList<String>> objectConstructor = constructorConstructor.newUnsafeAllocator(type, rawType);
        assertNotNull(objectConstructor);
    }

}