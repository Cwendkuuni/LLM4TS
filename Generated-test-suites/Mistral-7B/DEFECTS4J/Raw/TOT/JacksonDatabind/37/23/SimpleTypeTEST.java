import org.junit.Test;import static org.junit.Assert.*;import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<Integer> intClass = Integer.class;
 SimpleType intType = new SimpleType(intClass);
 assertNotNull(intType);
 assertEquals(intClass, intType.getRawClass());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 Class<Integer> intClass = Integer.class;
 SimpleType intType1 = new SimpleType(intClass);
 SimpleType intType2 = new SimpleType(intType1);
 assertNotNull(intType2);
 assertEquals(intType1, intType2);
 }

 @Test
 public void testSimpleTypeNarrowing() {
 Class<Integer> intClass = Integer.class;
 Class<Integer[]> intArrayClass = int[].class;
 SimpleType intType = new SimpleType(intClass);
 JavaType intArrayType = intType.narrow(intArrayClass);
 assertNotNull(intArrayType);
 assertFalse(intType.equals(intArrayType));
 assertEquals(intArrayClass, intArrayType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidConstruction() {
 Class<Map<String, Integer>> mapClass = Map.class;
 SimpleType mapType = SimpleType.construct(mapClass);
 fail("Should not reach this point");
 }

 @Test
 public void testSimpleTypeToString() {
 Class<Integer> intClass = Integer.class;
 SimpleType intType = new SimpleType(intClass);
 String intTypeString = intType.toString();
 assertEquals("[simple type, class [I]", intTypeString);
 }

 @Test
 public void testSimpleTypeEquals() {
 Class<Integer> intClass1 = Integer.class;
 Class<Integer> intClass2 = Integer.class;
 SimpleType intType1 = new SimpleType(intClass1);
 SimpleType intType2 = new SimpleType(intClass2);
 SimpleType intType3 = intType1;
 SimpleType intType4 = new SimpleType(intClass1);

 assertEquals(intType1, intType1);
 assertEquals(intType1, intType3);
 assertNotEquals(intType1, intType2);
 assertNotEquals(intType1, intType4);
 }

 @Test
 public void testSimpleTypeGetCanonicalName() {
 Class<Integer> intClass = Integer.class;
 SimpleType intType = new SimpleType(intClass);
 String intTypeCanonicalName = intType.getCanonicalName();
 assertEquals("[Ljava/lang/Integer;", intTypeCanonicalName);
 }

 @Test
 public void testSimpleTypeGetErasedSignature() {
 Class<Integer> intClass = Integer.class;
 SimpleType intType = new SimpleType(intClass);
 StringBuilder intTypeErasedSignature = new StringBuilder(40);
 intType.getErasedSignature(intTypeErasedSignature);
 assertEquals("[I", intTypeErasedSignature.toString());
 }

 @Test
 public void testSimpleTypeGetGenericSignature() {
 Class<Integer> intClass = Integer.class;
 SimpleType intType = new SimpleType(intClass);
 StringBuilder intTypeGenericSignature = new StringBuilder(40);
 intType.getGenericSignature(intTypeGenericSignature);
 assertEquals("[I]", intTypeGenericSignature.toString());
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 Class<Integer> intClass = Integer.class;
 Object intHandler = new Object() {};
 SimpleType intType = new SimpleType(intClass);
 JavaType intTypeWithHandler = intType.withTypeHandler(intHandler);
 assertNotNull(intTypeWithHandler);
 assertFalse(intType.equals(intTypeWithHandler));
 }

}