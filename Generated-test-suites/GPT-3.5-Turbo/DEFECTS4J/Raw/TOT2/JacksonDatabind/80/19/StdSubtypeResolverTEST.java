import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

  @Test
  public void testRegisterSubtypes() {
    StdSubtypeResolver resolver = new StdSubtypeResolver();
    NamedType type1 = new NamedType(Integer.class, "Integer");
    NamedType type2 = new NamedType(String.class, "String");
    
    resolver.registerSubtypes(type1, type2);
    
    assertEquals(2, resolver._registeredSubtypes.size());
  }

  @Test
  public void testCollectAndResolveSubtypesByClass() {
    StdSubtypeResolver resolver = new StdSubtypeResolver();
    MapperConfig<?> config = new MapperConfig<>();
    AnnotatedMember member = new AnnotatedMember();
    JavaType baseType = JavaType.construct(Integer.class);
    
    Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, member, baseType);
    
    assertNotNull(subtypes);
  }

  @Test
  public void testCollectAndResolveSubtypesByTypeId() {
    StdSubtypeResolver resolver = new StdSubtypeResolver();
    MapperConfig<?> config = new MapperConfig<>();
    AnnotatedMember member = new AnnotatedMember();
    JavaType baseType = JavaType.construct(Integer.class);
    
    Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, member, baseType);
    
    assertNotNull(subtypes);
  }

  @Test
  public void testInternalMethods() {
    StdSubtypeResolver resolver = new StdSubtypeResolver();
    AnnotatedMember member = new AnnotatedMember();
    NamedType type = new NamedType(Integer.class, "Integer");
    MapperConfig<?> config = new MapperConfig<>();
    AnnotationIntrospector ai = new AnnotationIntrospector();
    HashMap<NamedType, NamedType> collectedSubtypes = new HashMap<>();
    
    resolver._collectAndResolve(member.getAnnotatedClass(), type, config, ai, collectedSubtypes);
    
    assertNotNull(collectedSubtypes);
  }

  @Test
  public void testCombineNamedAndUnnamed() {
    StdSubtypeResolver resolver = new StdSubtypeResolver();
    Class<?> rawBase = Integer.class;
    Set<Class<?>> typesHandled = new HashSet<>();
    Map<String, NamedType> byName = new LinkedHashMap<>();
    
    Collection<NamedType> combined = resolver._combineNamedAndUnnamed(rawBase, typesHandled, byName);
    
    assertNotNull(combined);
  }

}