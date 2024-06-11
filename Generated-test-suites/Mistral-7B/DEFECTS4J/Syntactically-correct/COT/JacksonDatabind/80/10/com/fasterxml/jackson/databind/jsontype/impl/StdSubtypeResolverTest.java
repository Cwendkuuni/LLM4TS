package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig.DefaultTyping;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.util.AnnotatedClassResolver;

public class StdSubtypeResolverTest {

	@Test
	public void testRegisterSubtypesByClass() {
		StdSubtypeResolver resolver = new StdSubtypeResolver();
		// Test empty registration
		resolver.registerSubtypes(new Class[] {});
		assertEquals(0, resolver.getRegisteredSubtypes().size());

		// Test registration of single subtype
		resolver.registerSubtypes(new Class[] { MySubclass.class });
		assertEquals(1, resolver.getRegisteredSubtypes().size());
		assertEquals(MySubclass.class, resolver.getRegisteredSubtypes().iterator().next().getType());

		// Test registration of multiple subtypes
		resolver.registerSubtypes(new Class[] { MySubclass1.class, MySubclass2.class });
		assertEquals(2, resolver.getRegisteredSubtypes().size());
		assertTrue(resolver.getRegisteredSubtypes().contains(MySubclass1.class));
		assertTrue(resolver.getRegisteredSubtypes().contains(MySubclass2.class));
	}

	@Test
	public void testRegisterSubtypesByAnnotation() {
		StdSubtypeResolver resolver = new StdSubtypeResolver();
		// Test empty registration
		resolver.registerSubtypes(new NamedType[] {});
		assertEquals(0, resolver.getRegisteredSubtypes().size());

		// Test registration of single subtype
		resolver.registerSubtypes(new NamedType[] { new NamedType(MySubclass1.class) });
		assertEquals(1, resolver.getRegisteredSubtypes().size());
		assertEquals(MySubclass1.class, resolver.getRegisteredSubtypes().iterator().next().getType());

		// Test registration of multiple subtypes
		resolver.registerSubtypes(new NamedType[] { new NamedType(MySubclass1.class), new NamedType(MySubclass2.class) });
		assertEquals(2, resolver.getRegisteredSubtypes().size());
		assertTrue(resolver.getRegisteredSubtypes().contains(MySubclass1.class));
		assertTrue(resolver.getRegisteredSubtypes().contains(MySubclass2.class));
	}

	@Test
	public void testCollectAndResolveSubtypesByClass() {
		StdSubtypeResolver resolver = new StdSubtypeResolver();

		// Test empty collection
		assertEquals(Collections.emptyList(), resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder()
				.build(), new AnnotatedMember(MyBaseClass.class, ""), null));

		// Test no registered subtypes
		resolver.registerSubtypes(new Class[] { MySubclass1.class, MySubclass2.class });
		assertEquals(Collections.emptyList(), resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder()
				.build(), new AnnotatedMember(MyBaseClass.class, ""), new JavaType(MyBaseClass.class)));

		// Test registered subtypes
		resolver = new StdSubtypeResolver();
		resolver.registerSubtypes(new Class[] { MySubclass1.class, MySubclass2.class });
		List<NamedType> result = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder()
				.build(), new AnnotatedMember(MyBaseClass.class, ""), new JavaType(MyBaseClass.class));
		assertEquals(2, result.size());
		assertTrue(result.contains(new NamedType(MySubclass1.class)));
		assertTrue(result.contains(new NamedType(MySubclass2.class)));

		// Test base type itself
		result = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder()
				.build(), new AnnotatedMember(MyBaseClass.class, ""), new JavaType(MyBaseClass.class));
		assertEquals(1, result.size());
		assertEquals(MyBaseClass.class, result.get(0).getType());
	}

	@Test
	public void testCollectAndResolveSubtypesByClassWithAnnotation() {
		StdSubtypeResolver resolver = new StdSubtypeResolver();

		// Test empty collection
		assertEquals(Collections.emptyList(), resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder()
				.build(), new AnnotatedClass(MyBaseClassWithAnnotation.class), null));

		// Test no registered subtypes
		resolver.registerSubtypes(new Class[] { MySubclass1.class, MySubclass2.class });
		assertEquals(Collections.emptyList(), resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder()
				.build(), new AnnotatedClass(MyBaseClassWithAnnotation.class), new JavaType(MyBaseClassWithAnnotation.class)));

		// Test registered subtypes
		resolver = new StdSubtypeResolver();
		resolver.registerSubtypes(new Class[] { MySubclass1.class, MySubclass2.class });
		List<NamedType> result = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder()
				.build(), new AnnotatedClass(MyBaseClassWithAnnotation.class), new JavaType(MyBaseClassWithAnnotation.class));
		assertEquals(2, result.size());
		assertTrue(result.contains(new NamedType(MySubclass1.class)));
		assertTrue(result.contains(new NamedType(MySubclass2.class)));

		// Test base type itself
		result = resolver.collectAndResolveSubtypesByClass(new MapperConfig.Builder()
				.build(), new AnnotatedClass(MyBaseClassWithAnnotation.class), new JavaType(MyBaseClassWithAnnotation.class));
		assertEquals(1, result.size());
		assertEquals(MyBaseClassWithAnnotation.class, result.get(0).getType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCollectAndResolveSubtypesByClassWithNullMapperConfig() {
		new StdSubtypeResolver().collectAndResolveSubtypesByClass(null, new AnnotatedMember(MyBaseClass.class, ""), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCollectAndResolveSubtypesByClassWithNullAnnotatedMember() {
		new StdSubtypeResolver().collectAndResolveSubtypesByClass(new MapperConfig.Builder().build(), null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCollectAndResolveSubtypesByClassWithNullJavaType() {
		new StdSubtypeResolver().collectAndResolveSubtypesByClass(new MapperConfig.Builder().build(),
				new AnnotatedMember(MyBaseClass.class, ""), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCollectAndResolveSubtypesByClassWithNullAnnotatedClass() {
		new StdSubtypeResolver().collectAndResolveSubtypesByClass(new MapperConfig.Builder().build(), null,
				new JavaType(MyBaseClass.class));
	}

	@Test
	public void testCollectAndResolveSubtypesByTypeId() {
		StdSubtypeResolver resolver = new StdSubtypeResolver();

		// Test empty collection
		assertEquals(Collections.emptyList(), resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder()
				.build(), new AnnotatedMember(MyBaseClass.class, ""), null));

		// Test no registered subtypes
		resolver.registerSubtypes(new Class[] { MySubclass1.class, MySubclass2.class });
		assertEquals(Collections.emptyList(), resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder()
				.build(), new AnnotatedMember(MyBaseClass.class, ""), new JavaType(MyBaseClass.class)));

		// Test registered subtypes
		resolver = new StdSubtypeResolver();
		resolver.registerSubtypes(new Class[] { MySubclass1.class, MySubclass2.class });
		List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder()
				.build(), new AnnotatedMember(MyBaseClass.class, ""), new JavaType(MyBaseClass.class));
		assertEquals(2, result.size());
		assertTrue(result.contains(new NamedType(MySubclass1.class)));
		assertTrue(result.contains(new NamedType(MySubclass2.class)));

		// Test base type itself
		result = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder()
				.build(), new AnnotatedMember(MyBaseClass.class, ""), new JavaType(MyBaseClass.class));
		assertEquals(1, result.size());
		assertEquals(MyBaseClass.class, result.get(0).getType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCollectAndResolveSubtypesByTypeIdWithNullMapperConfig() {
		new StdSubtypeResolver().collectAndResolveSubtypesByTypeId(null, new AnnotatedMember(MyBaseClass.class, ""), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCollectAndResolveSubtypesByTypeIdWithNullAnnotatedMember() {
		new StdSubtypeResolver().collectAndResolveSubtypesByTypeId(new MapperConfig.Builder().build(), null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCollectAndResolveSubtypesByTypeIdWithNullJavaType() {
		new StdSubtypeResolver().collectAndResolveSubtypesByTypeId(new MapperConfig.Builder().build(),
				new AnnotatedMember(MyBaseClass.class, ""), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCollectAndResolveSubtypesByTypeIdWithNullAnnotatedClass() {
		new StdSubtypeResolver().collectAndResolveSubtypesByTypeId(new MapperConfig.Builder().build(),
				new AnnotatedMember(MyBaseClass.class, ""), new JavaType(MyBaseClass.class));
	}

	@Test
	public void testCollectAndResolveSubtypesByTypeIdWithAnnotation() {
		StdSubtypeResolver resolver = new StdSubtypeResolver();

		// Test empty collection
		assertEquals(Collections.emptyList(), resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder()
				.build(), new AnnotatedClass(MyBaseClassWithAnnotation.class), null));

		// Test no registered subtypes
		resolver.registerSubtypes(new Class[] { MySubclass1.class, MySubclass2.class });
		assertEquals(Collections.emptyList(), resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder()
				.build(), new AnnotatedClass(MyBaseClassWithAnnotation.class), new JavaType(MyBaseClassWithAnnotation.class)));

		// Test registered subtypes
		resolver = new StdSubtypeResolver();
		resolver.registerSubtypes(new Class[] { MySubclass1.class, MySubclass2.class });
		List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder()
				.build(), new AnnotatedClass(MyBaseClassWithAnnotation.class), new JavaType(MyBaseClassWithAnnotation.class));
		assertEquals(2, result.size());
		assertTrue(result.contains(new NamedType(MySubclass1.class)));
		assertTrue(result.contains(new NamedType(MySubclass2.class)));

		// Test base type itself
		result = resolver.collectAndResolveSubtypesByTypeId(new MapperConfig.Builder()
				.build(), new AnnotatedClass(MyBaseClassWithAnnotation.class), new JavaType(MyBaseClassWithAnnotation.class));
		assertEquals(1, result.size());
		assertEquals(MyBaseClassWithAnnotation.class, result.get(0).getType());
	}

	private static class MyBaseClass {
	}

	@MyTypeName("Subclass1")
	private static class MySubclass1 extends MyBaseClass {
	}

	@MyTypeName("Subclass2")
	private static class MySubclass2 extends MyBaseClass {
	}

	@MyTypeName("BaseClassWithAnnotation")
	private static class MyBaseClassWithAnnotation extends MyBaseClass {
	}

	@MyTypeName("Subclass1WithAnnotation")
	private static class MySubclass1WithAnnotation extends MySubclass1 {
	}

	@MyTypeName("Subclass2WithAnnotation")
	private static class MySubclass2WithAnnotation extends MySubclass2 {
	}

	public static class MyMapperConfig extends MapperConfig.Builder {
		public MyMapperConfig() {
			defaultTyping(DefaultTyping.NON_FINAL);
		}
	}
}