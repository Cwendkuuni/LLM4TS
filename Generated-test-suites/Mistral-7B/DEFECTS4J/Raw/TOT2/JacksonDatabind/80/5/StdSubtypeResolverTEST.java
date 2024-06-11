import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdSubtypeResolverTest {

 private static final MapperConfig<?> config = new MapperConfig.Standard();

 @Test
 public void testRegisterSubtypesByClass() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();

 // Test registering empty list of subtypes
 resolver.registerSubtypes();
 assertNull(resolver.getRegisteredSubtypes());

 // Test registering subtypes by class
 Class<?> parentType = Parent.class;
 Class<?> subtype1 = Subtype1.class;
 Class<?> subtype2 = Subtype2.class;
 resolver.registerSubtypes(subtype1, subtype2);
 List<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();
 assertEquals(2, registeredSubtypes.size());
 assertNotNull(registeredSubtypes.get(0).getType());
 assertEquals(Subtype1.class, registeredSubtypes.get(0).getType());
 equal(subtype1.getName(), registeredSubtypes.get(0).getName());
 assertEquals(subtype2.getName(), registeredSubtypes.get(1).getName());
 assertEquals(Parent.class, registeredSubtypes.get(1).getType().getSuperclass());
 }

 @Test
 public void testRegisterSubtypesByAnnotations() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();

 // Test registering empty list of subtypes
 resolver.registerSubtypes();
 assertNull(resolver.getRegisteredSubtypes());

 // Test registering subtypes by annotations
 Parent parent = new Parent();
 Subtype1 subtype1 = new Subtype1();
 Subtype2 subtype2 = new Subtype2();
 AnnotatedMember property = new AnnotatedMethod(Parent.class, "getSubtype", new Class[0]);
 JavaType baseType = JavaType.construct(Parent.class);
 resolver.collectAndResolveSubtypesByClass(config, property, baseType);
 List<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();
 assertEquals(2, registeredSubtypes.size());
 assertNotNull(registeredSubtypes.get(0).getType());
 assertEquals(Subtype1.class, registeredSubtypes.get(0).getType());
 assertEquals(Subtype1.class.getName(), registeredSubtypes.get(0).getName());
 assertEquals(Parent.class, registeredSubtypes.get(1).getType().getSuperclass());
 assertEquals(Subtype2.class.getName(), registeredSubtypes.get(1).getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterSubtypesWithNullTypes() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 resolver.registerSubtypes((NamedType[]) null);
 }

 @Test
 public void testCollectAndResolveSubtypesByClass() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes(Subtype1.class, Subtype2.class);

 JavaType baseType = JavaType.construct(Parent.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, new AnnotatedMember(Parent.class, "getSubtype", new Class[0]), baseType);
 assertEquals(2, subtypes.size());
 assertNotNull(subtypes.get(0).getName());
 assertEquals(Subtype1.class.getName(), subtypes.get(0).getName());
 assertNotNull(subtypes.get(1).getName());
 assertEquals(Subtype2.class.getName(), subtypes.get(1).getName());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassEmptyList() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes();

 JavaType baseType = JavaType.construct(Parent.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, new AnnotatedMember(Parent.class, "getSubtype", new Class[0]), baseType);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassNoSubtypes() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes();

 JavaType baseType = JavaType.construct(AnotherClass.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, new AnnotatedMember(Parent.class, "getSubtype", new Class[0]), baseType);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeId() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes(Subtype1.class, Subtype2.class);

 JavaType baseType = JavaType.construct(Parent.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(Parent.class, "getSubtype", new Class[0]), baseType);
 assertEquals(2, subtypes.size());
 assertNotNull(subtypes.get(0).getName());
 assertEquals(Subtype1.class.getName(), subtypes.get(0).getName());
 assertNotNull(subtypes.get(1).getName());
 assertEquals(Subtype2.class.getName(), subtypes.get(1).getName());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdEmptyList() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes();

 JavaType baseType = JavaType.construct(Parent.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(Parent.class, "getSubtype", new Class[0]), baseType);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdNoSubtypes() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes();

 JavaType baseType = JavaType.construct(AnotherClass.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(Parent.class, "getSubtype", new Class[0]), baseType);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdNullBaseType() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes();

 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(Parent.class, "getSubtype", new Class[0]), null);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdNullProperty() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes();

 JavaType baseType = JavaType.construct(Parent.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithAbstractClass() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes(Subtype1.class, Subtype2.class);

 JavaType baseType = JavaType.construct(AbstractBase.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(Parent.class, "getSubtype", new Class[0]), baseType);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithSubtype() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes(Subtype1.class, Subtype2.class);

 JavaType baseType = JavaType.construct(Subtype1.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(Parent.class, "getSubtype", new Class[0]), baseType);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithNonRegisteredSubtype() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes(Subtype1.class, Subtype2.class);

 JavaType baseType = JavaType.construct(AnotherClass.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(Parent.class, "getSubtype", new Class[0]), baseType);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithInheritedSubtypes() {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Parent parent = new Parent();
 resolver.registerSubtypes(Subtype1.class, Subtype2.class);

 JavaType baseType = JavaType.construct(Parent.class);
 List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(Parent.class, "getSubtype", new Class[0]), baseType);
 assertEquals(2, subtypes.size());
 assertNotNull(subtypes.get(0).getName());
 assertEquals(Subtype1.class.getName(), subtypes.get(0).getName());
 assertNotNull(subtypes.get(1).getName());
 assertEquals(Subtype2.class.getName(), subtypes.get(1).getName());
 }
}