/*
 *
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1999.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1997-1999
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Bob Jervis
 *   Google Inc.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU General Public License Version 2 or later (the "GPL"), in which
 * case the provisions of the GPL are applicable instead of those above. If
 * you wish to allow use of your version of this file only under the terms of
 * the GPL and not to allow others to use your version of this file under the
 * MPL, indicate your decision by deleting the provisions above and replacing
 * them with the notice and other provisions required by the GPL. If you do
 * not delete the provisions above, a recipient may use your version of this
 * file under either the MPL or the GPL.
 *
 * ***** END LICENSE BLOCK ***** */
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsSubtype() {
        // Create record types for testing
        RecordType type1 = createRecordType1();
        RecordType type2 = createRecordType2();
        RecordType type3 = createRecordType3();
        RecordType type4 = createRecordType4();
        
        // Test cases for isSubtype() method
        assertTrue(type1.isSubtype(type2));
        assertFalse(type2.isSubtype(type1));
        assertTrue(type2.isSubtype(type3));
        assertTrue(type3.isSubtype(type4));
        assertFalse(type4.isSubtype(type3));
    }
    
    @Test
    public void testDefineProperty() {
        // Create record types for testing
        RecordType type1 = createRecordType1();
        
        // Test cases for defineProperty() method
        assertTrue(type1.defineProperty("a", createStringType(), false, null));
        assertFalse(type1.defineProperty("b", createStringType(), false, null));       
    }
    
    @Test
    public void testResolveInternal() {
        // Create record types for testing
        RecordType type1 = createRecordType1();
        RecordType type2 = createRecordType2();
        
        // Test cases for resolveInternal() method
        assertEquals(type1, type1.resolveInternal(null, null));
        assertEquals(type2, type2.resolveInternal(null, null));
    }

    private RecordType createRecordType1() {
        return new RecordType(null, null, true);
    }
    
    private RecordType createRecordType2() {
        return new RecordType(null, null, false);
    }
    
    private RecordType createRecordType3() {
        return new RecordType(null, null, true);
    }
    
    private RecordType createRecordType4() {
        return new RecordType(null, null, false);
    }
    
    private JSType createStringType() {
        return new JSType(null, null);
    }

}