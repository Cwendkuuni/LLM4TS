
import com.fasterxml.jackson.databind.jsontype.impl.NamedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

	StdSubtypeResolver subtypeResolver;

	@Before
	public void setUp() {
		subtypeResolver = new StdSubtypeResolver();
	}

	@Test
	public void testRegisterSubtypes() {
		NamedType namedType = new NamedType(String.class);
		subtypeResolver.registerSubtypes(namedType);

		Assert.assertEquals(1, subtypeResolver._registeredSubtypes.size());
		Assert.assertTrue(subtypeResolver._registeredSubtypes.contains(namedType));
	}

	@Test
	public void testRegisterSubtypesArray() {
		NamedType namedType1 = new NamedType(String.class);
		NamedType namedType2 = new NamedType(Integer.class);
		subtypeResolver.registerSubtypes(namedType1, namedType2);

		Assert.assertEquals(2, subtypeResolver._registeredSubtypes.size());
		Assert.assertTrue(subtypeResolver._registeredSubtypes.contains(namedType1));
		Assert.assertTrue(subtypeResolver._registeredSubtypes.contains(namedType2));
	}

	@Test
	public void testCollectAndResolveSubtypesByClassWithNullBaseType() {
		MapperConfig<?> config = null;
		AnnotatedMember property = null;
		JavaType baseType = null;

		Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testCollectAndResolveSubtypesByClass() {
		MapperConfig<?> config = null;
		AnnotatedMember property = null;
		JavaType baseType = new JavaType(String.class);

		Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testCollectAndResolveSubtypesByClassWithConfig() {
		MapperConfig<?> config = null;
		AnnotatedClass type = null;

		Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, type);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testCollectAndResolveSubtypesByTypeIdWithNullBaseType() {
		MapperConfig<?> config = null;
		AnnotatedMember property = null;
		JavaType baseType = null;

		Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testCollectAndResolveSubtypesByTypeId() {
		MapperConfig<?> config = null;
		AnnotatedMember property = null;
		JavaType baseType = new JavaType(String.class);

		Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testCollectAndResolveSubtypesByTypeIdWithConfig() {
		MapperConfig<?> config = null;
		AnnotatedClass baseType = null;

		Collection<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, baseType);

		Assert.assertEquals(0, result.size());
	}
}
