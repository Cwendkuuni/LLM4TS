import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdSubtypeResolverTest {

 @Test
 public void testRegisterSubtypesByClass() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 Class<?> baseType = String.class;
 Class<?> subtype1 = Integer.class;
 Class<?> subtype2 = Long.class;

 // Register subtypes by class
 resolver.registerSubtypes(subtype1, subtype2);

 JavaType baseTypeType = ObjectMapper.getDefaultInstance().getTypeFactory()
 .constructType(baseType);
 MapperConfig<?> config = ObjectMapper.getDefaultInstance().getConfig();

 // Test collectAndResolveSubtypesByClass
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseTypeType);
 assertEquals(2, subtypes.size());
 for (NamedType namedType : subtypes) {
 assertNotNull(namedType.getName());
 if (namedType.getType().isAssignableFrom(subtype1)) {
 assertEquals("Integer", namedType.getName());
 } else if (namedType.getType().isAssignableFrom(subtype2)) {
 assertEquals("Long", namedType.getName());
 }
 }
 }

 @Test
 public void testRegisterSubtypesByName() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 String baseTypeName = "java.lang.String";
 String subtype1Name = "java.lang.Integer";
 String subtype2Name = "java.lang.Long";

 // Register subtypes by name
 resolver.registerSubtypes(new NamedType(String.class, "String"),
 new NamedType(Integer.class, "Integer"),
 new NamedType(Long.class, "Long"));

 MapperConfig<?> config = ObjectMapper.getDefaultInstance().getConfig();
 JavaType baseTypeType = ObjectMapper.getDefaultInstance().getTypeFactory()
 .constructType(Class.forName(baseTypeName));

 // Test collectAndResolveSubtypesByClass
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseTypeType);
 assertEquals(3, subtypes.size());
 for (NamedType namedType : subtypes) {
 assertNotNull(namedType.getName());
 if (namedType.getType().isAssignableFrom(Integer.class)) {
 assertEquals("Integer", namedType.getName());
 } else if (namedType.getType().isAssignableFrom(Long.class)) {
 assertEquals("Long", namedType.getName());
 } else if (namedType.getType().isAssignableFrom(String.class)) {
 assertEquals("String", namedType.getName());
 }
 }
 }

 @Test
 public void testCollectAndResolveSubtypesByClass_EmptyRegisteredSubtypes() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = ObjectMapper.getDefaultInstance().getConfig();
 JavaType baseTypeType = ObjectMapper.getDefaultInstance().getTypeFactory()
 .constructType(String.class);

 // Test collectAndResolveSubtypesByClass with empty registered subtypes
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseTypeType);
 assertEquals(0, subtypes.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCollectAndResolveSubtypesByClass_NullBaseType() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = ObjectMapper.getDefaultInstance().getConfig();

 // Test collectAndResolveSubtypesByClass with null baseType
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, null);
 }

 @Test
 public void testCollectAndResolveSubtypesByClassId_EmptyRegisteredSubtypes() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = ObjectMapper.getDefaultInstance().getConfig();
 JavaType baseTypeType = ObjectMapper.getDefaultInstance().getTypeFactory()
 .constructType(String.class);

 // Test collectAndResolveSubtypesByClassId with empty registered subtypes
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClassId(config, null, baseTypeType, null);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassId_NullBaseType() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = ObjectMapper.getDefaultInstance().getConfig();

 // Test collectAndResolveSubtypesByClassId with null baseType
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClassId(config, null, null, null);
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeId_EmptyRegisteredSubtypes() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = ObjectMapper.getDefaultInstance().getConfig();
 JavaType baseTypeType = ObjectMapper.getDefaultInstance().getTypeFactory()
 .constructType(String.class);

 // Test collectAndResolveSubtypesByTypeId with empty registered subtypes
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseTypeType);
 assertEquals(0, subtypes.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeId_NullBaseType() throws Exception {
 StdSubtypeResolver resolver = new StdSubtypeResolver();
 MapperConfig<?> config = ObjectMapper.getDefaultInstance().getConfig();

 // Test collectAndResolveSubtypesByTypeId with null baseType
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, null);
 }
}