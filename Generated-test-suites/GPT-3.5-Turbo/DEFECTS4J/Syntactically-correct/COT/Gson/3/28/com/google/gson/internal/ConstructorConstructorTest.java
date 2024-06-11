import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
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
import com.google.gson.JsonIOException;
import com.google.gson.internal.LinkedTreeMap;
import sun.reflect.ReflectionFactory;

public class ConstructorConstructorTest {

	@Test
	public void testGet() {
		Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
		ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
		TypeToken<Object> typeToken = TypeToken.get(Object.class);
		ObjectConstructor<Object> expected = constructorConstructor.get(typeToken);
		assertNotNull(expected);
	}

	@Test
	public void testNewDefaultConstructor() {
		Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
		ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
		Class<Object> rawType = Object.class;
		ObjectConstructor<Object> expected = constructorConstructor.newDefaultConstructor(rawType);
		assertNull(expected);
	}

	@Test
	public void testNewDefaultImplementationConstructor() {
		Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
		ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
		Type type = TypeToken.get(Object.class).getType();
		Class<Object> rawType = Object.class;
		ObjectConstructor<Object> expected = constructorConstructor.newDefaultImplementationConstructor(type,
				rawType);
		assertNull(expected);
	}

	@Test
	public void testNewUnsafeAllocator() {
		Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
		ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
		Type type = TypeToken.get(Object.class).getType();
		Class<Object> rawType = Object.class;
		ObjectConstructor<Object> expected = constructorConstructor.newUnsafeAllocator(type, rawType);
		assertNotNull(expected);
	}

	@Test
	public void testToString() {
		Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
		ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
		String expected = constructorConstructor.toString();
		assertNotNull(expected);
	}

}