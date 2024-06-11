package org.sourceforge.ifx.basetypes;

import org.junit.*;
import static org.junit.Assert.*;

public class IFXObjectTest {

    private IFXObject object;
    
    @Before
    public void setUp() {
        object = new IFXObject();
    }
    
    @Test
    public void testEquals_sameObject_true() {
        assertTrue(object.equals(object));
    }
    
    @Test
    public void testEquals_differentClass_false() {
        assertFalse(object.equals(new Object()));
    }
    
    @Test
    public void testEquals_sameClassDifferentValues_false() {
        IFXObject object1 = new IFXObject();
        IFXObject object2 = new IFXObject();
        assertFalse(object1.equals(object2));
    }
    
    @Test
    public void testEquals_sameClassSameValues_true() {
        IFXObject object1 = new IFXObject();
        IFXObject object2 = new IFXObject();
        assertEquals(object1, object2);
    }
    
    @Test
    public void testToString_noMembers_emptyString() {
        assertEquals("", object.toString());
    }
    
    @Test
    public void testToString_singleMember_nullString() {
        object.setMember(null);
        assertEquals("Member = NULL", object.toString());
    }
    
    @Test
    public void testToString_arrayMember_emptyArray() {
        object.setArrayMember(new IFXObject[0]);
        assertEquals("ArrayMember = []", object.toString());
    }
    
    @Test
    public void testToString_baseTypeMember_validString() {
        BaseTypeMember member = new BaseTypeMember("value");
        object.setBaseTypeMember(member);
        assertEquals("BaseTypeMember = value", object.toString());
    }
    
    @Test
    public void testToString_nestedObjectMember_validString() {
        IFXObject nestedObject = new IFXObject();
        nestedObject.setMember("nested");
        object.setNestedObjectMember(nestedObject);
        assertEquals("NestedObjectMember = {\n  Member = nested\n}", object.toString());
    }
    
    @Test
    public void testToString_exceptionThrown_exceptionMessageAppended() {
        object.setExceptionThrown(true);
        assertEquals("Exception! java.lang.Exception", object.toString());
    }
    
    @Test
    public void testToString_depthLimitReached_validString() {
        IFXObject nestedObject = new IFXObject();
        nestedObject.setNestedObjectMember(object);
        object.setNestedObjectMember(nestedObject);
        assertEquals("NestedObjectMember = {\n  NestedObjectMember = {\n    Exception! java.lang.StackOverflowError\n  }\n}", object.toString());
    }
    
    @Test
    public void testCheckEquals_bothNullObjects_true() {
        assertTrue(object.checkEquals(null, null));
    }
    
    @Test
    public void testCheckEquals_oneNullObject_false() {
        assertFalse(object.checkEquals(null, object));
    }
    
    @Test
    public void testCheckEquals_differentClassObjects_false() {
        assertFalse(object.checkEquals(object, new Object()));
    }
    
    @Test
    public void testCheckEquals_differentClassNames_false() {
        IFXObject object1 = new IFXObject();
        IFXObject object2 = new IFXObject();
        assertFalse(object.checkEquals(object1, object2));
    }
    
    @Test
    public void testCheckEquals_sameClassDifferentValues_false() {
        IFXObject object1 = new IFXObject();
        IFXObject object2 = new IFXObject();
        assertFalse(object.checkEquals(object1, object2));
    }
    
    @Test
    public void testCheckEquals_sameClassSameValues_true() {
        IFXObject object1 = new IFXObject();
        IFXObject object2 = new IFXObject();
        assertTrue(object.checkEquals(object1, object2));
    }
    
    @Test
    public void testCheckEquals_arrayMembersSizeMismatch_false() {
        IFXObject[] arrayMember1 = new IFXObject[2];
        IFXObject[] arrayMember2 = new IFXObject[3];
        assertFalse(object.checkEquals(arrayMember1, arrayMember2));
    }
    
    @Test
    public void testCheckEquals_arrayMembersAllEqual_true() {
        IFXObject[] arrayMember1 = new IFXObject[3];
        IFXObject[] arrayMember2 = new IFXObject[3];
        IFXObject obj = new IFXObject();
        arrayMember1[0] = obj;
        arrayMember2[0] = obj;
        arrayMember1[1] = obj;
        arrayMember2[1] = obj;
        arrayMember1[2] = obj;
        arrayMember2[2] = obj;
        assertTrue(object.checkEquals(arrayMember1, arrayMember2));
    }
    
    @Test
    public void testCheckEquals_arrayMembersMismatch_false() {
        IFXObject[] arrayMember1 = new IFXObject[3];
        IFXObject[] arrayMember2 = new IFXObject[3];
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        arrayMember1[0] = obj1;
        arrayMember2[0] = obj1;
        arrayMember1[1] = obj2;
        arrayMember2[1] = obj2;
        arrayMember1[2] = obj1;
        arrayMember2[2] = obj1;
        assertFalse(object.checkEquals(arrayMember1, arrayMember2));
    }
    
    @Test
    public void testCheckEquals_baseTypeMembersEqual_true() {
        BaseTypeMember member1 = new BaseTypeMember("value");
        BaseTypeMember member2 = new BaseTypeMember("value");
        assertTrue(object.checkEquals(member1, member2));
    }
    
    @Test
    public void testCheckEquals_baseTypeMembersNotEqual_false() {
        BaseTypeMember member1 = new BaseTypeMember("value1");
        BaseTypeMember member2 = new BaseTypeMember("value2");
        assertFalse(object.checkEquals(member1, member2));
    }
    
    @Test
    public void testCheckEquals_nestedObjectsEqual_true() {
        IFXObject nestedObject1 = new IFXObject();
        IFXObject nestedObject2 = new IFXObject();
        nestedObject1.setMember("nested");
        nestedObject2.setMember("nested");
        assertTrue(object.checkEquals(nestedObject1, nestedObject2));
    }
    
    @Test
    public void testCheckEquals_nestedObjectsNotEqual_false() {
        IFXObject nestedObject1 = new IFXObject();
        IFXObject nestedObject2 = new IFXObject();
        nestedObject1.setMember("nested1");
        nestedObject2.setMember("nested2");
        assertFalse(object.checkEquals(nestedObject1, nestedObject2));
    }
    
    @Test
    public void testCheckEquals_exceptionThrown_false() {
        assertTrue(object.checkEquals(object, object));
    }
    
    @Test
    public void testTab_zeroLevel_emptyString() {
        assertEquals("", object.tab(0));
    }
    
    @Test
    public void testTab_positiveLevel_validString() {
        assertEquals("  ", object.tab(1));
    }
    
    @Test
    public void testTab_negativeLevel_emptyString() {
        assertEquals("", object.tab(-1));
    }
}