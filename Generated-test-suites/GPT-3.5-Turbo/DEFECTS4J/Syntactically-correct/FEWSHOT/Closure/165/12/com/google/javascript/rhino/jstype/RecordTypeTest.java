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

package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ObjectType objectType = registry.getObjectType();
        ObjectType recordType1 = new RecordType(registry, null, null);
        ObjectType recordType2 = new RecordType(registry, null, null);
        assertFalse(recordType1.isEquivalentTo(objectType));
        assertTrue(recordType1.isEquivalentTo(recordType1));
        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ObjectType recordType = new RecordType(registry, null, null);
        assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ObjectType objectType = registry.getObjectType();
        ObjectType recordType1 = new RecordType(registry, null, null);
        ObjectType recordType2 = new RecordType(registry, null, null);
        assertFalse(recordType1.isSubtype(objectType));
        assertTrue(recordType1.isSubtype(recordType1));
        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ObjectType recordType = new RecordType(registry, null, null);
        ErrorReporter errorReporter = new ErrorReporter(registry);
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertSame(recordType, resolvedType);
    }
}