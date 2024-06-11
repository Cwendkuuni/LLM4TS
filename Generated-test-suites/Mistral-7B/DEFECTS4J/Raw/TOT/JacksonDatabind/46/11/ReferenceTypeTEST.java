import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testReferenceTypeConstruction() {
 Class<? > intType = Integer.TYPE;
 JavaType intJavaType = TypeReference.type(intType);
 ReferenceType referenceType = ReferenceType.construct(intType, intJavaType, null, null);
 assertEquals(intType, referenceType.getRawClass());
 assertEquals(intJavaType, referenceType.getContentType());
 }

 @Test
 public void testReferenceTypeTypeHandlers() {
 Class<? > intType = Integer.TYPE;
 JavaType intJavaType = TypeReference.type(intType);
 JavaType intJavaTypeWithHandler = objectMapper.getTypeFactory().constructType(new TypeReference<JavaType>() {}.getType());
 ReferenceType referenceType = ReferenceType.construct(intType, intJavaType, null, intJavaTypeWithHandler, false);
 JavaType newReferencedType = referenceType.withTypeHandler(new Object() {});
 assertNotSame(intJavaType, newReferencedType);
 }

 @Test
 public void testReferenceTypeValueHandlers() {
 Class<? > intType = Integer.TYPE;
 JavaType intJavaType = TypeReference.type(intType);
 ReferenceType referenceType = ReferenceType.construct(intType, intJavaType, new Object(), null, false);
 JavaType newReferenceType = referenceType.withValueHandler(new Object());
 assertNotSame(referenceType, newReferenceType);
 }

 @Test
 public void testReferenceTypeStaticTyping() {
 Class<? > intType = Integer.TYPE;
 JavaType intJavaType = TypeReference.type(intType);
 ReferenceType referenceType = ReferenceType.construct(intType, intJavaType, null, null, false);
 ReferenceType staticReferenceType = referenceType.withStaticTyping();
 assertTrue(staticReferenceType.isStatic());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReferenceTypeInvalidTypeHandler() {
 Class<? > intType = Integer.TYPE;
 JavaType intJavaType = TypeReference.type(intType);
 ReferenceType referenceType = ReferenceType.construct(intType, intJavaType, null, null, false);
 referenceType.withTypeHandler(new String("invalidHandler"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReferenceTypeInvalidValueHandler() {
 Class<? > intType = Integer.TYPE;
 JavaType intJavaType = TypeReference.type(intType);
 ReferenceType referenceType = ReferenceType.construct(intType, intJavaType, null, null, false);
 referenceType.withValueHandler(new String("invalidHandler"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReferenceTypeNullTypeHandler() {
 Class<? > intType = Integer.TYPE;
 JavaType intJavaType = TypeReference.type(intType);
 ReferenceType referenceType = ReferenceType.construct(intType, intJavaType, null, null, false);
 referenceType.withTypeHandler(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReferenceTypeNullValueHandler() {
 Class<? > intType = Integer.TYPE;
 JavaType intJavaType = TypeReference.type(intType);
 ReferenceType referenceType = ReferenceType.construct(intType, intJavaType, null, null, false);
 referenceType.withValueHandler(null);
 }

 @Test
 public void testReferenceTypeNarrowing() {
 Class<? > intType = Integer.TYPE;
 Class<? > intSubClassType = Integer.class;
 JavaType intJavaType = TypeReference.type(intType);
 JavaType intSubJavaType = TypeReference.type(intSubClassType);
 ReferenceType referenceType = ReferenceType.construct(intType, intJavaType, null, null, false);
 ReferenceType narrowedReferenceType = referenceType._narrow(intSubClassType);
 assertEquals(intSubJavaType, narrowedReferenceType.getContentType());
 }

 @Test
 public void testReferenceTypeGetReferencedType() {
 Class<? > intType = Integer.TYPE;
 JavaType intJavaType = TypeReference.type(intType);
 ReferenceType referenceType = ReferenceType.construct(intType, intJavaType, null, null, false);
 JavaType referencedType = referenceType.getReferencedType();
 assertEquals(intJavaType, referencedType);
 }

 @Test
 public void testReferenceTypeIsReferenceType() {
 Class<? > intType = Integer.TYPE;
 JavaType intJavaType = TypeReference.type(intType);
 ReferenceType referenceType = ReferenceType.construct(intType, intJavaType, null, null, false);
 assertTrue(referenceType.isReferenceType());
 }
}