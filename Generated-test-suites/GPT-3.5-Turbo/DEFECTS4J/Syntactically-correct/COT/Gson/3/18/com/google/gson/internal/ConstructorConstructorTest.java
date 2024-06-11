
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

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

import static org.junit.Assert.*;

public class ConstructorConstructorTest {

    @Test
    public void testGet() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.get(null));
    }

    @Test
    public void testNewDefaultConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.newDefaultConstructor(null));
    }

    @Test
    public void testNewDefaultImplementationConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.newDefaultImplementationConstructor(null, null));
    }

    @Test
    public void testNewUnsafeAllocator() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.newUnsafeAllocator(null, null));
    }

    @Test
    public void testToString() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals("null", constructorConstructor.toString());
    }

    @Test
    public void testConstructorConstructor() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertNotNull(constructorConstructor);
    }

    @Test
    public void testGetRawType() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.getRawType());
    }

    @Test
    public void testConstruct() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.construct());
    }

    @Test
    public void testCreateInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.createInstance(null));
    }

    @Test
    public void testConstruct1() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.construct(null));
    }

    @Test
    public void testNewInstance() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.newInstance(null));
    }

    @Test
    public void testNewDefaultConstructor1() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.newDefaultConstructor(null));
    }

    @Test
    public void testConstruct2() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.construct(null));
    }

    @Test
    public void testNewDefaultImplementationConstructor1() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.newDefaultImplementationConstructor(null, null));
    }

    @Test
    public void testConstruct3() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.construct(null));
    }

    @Test
    public void testNewUnsafeAllocator1() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
        assertEquals(null, constructorConstructor.newUnsafeAllocator(null, null));
    }

}
