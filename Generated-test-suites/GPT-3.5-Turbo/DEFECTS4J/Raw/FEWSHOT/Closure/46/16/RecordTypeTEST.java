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

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * A record (structural) type.
 *
 * Subtyping: The subtyping of a record type is defined via structural
 * comparison of a record type's properties. For example, a record
 * type of the form { a : TYPE_1 } is a supertype of a record type
 * of the form { b : TYPE_2, a : TYPE_1 } because B can be assigned to
 * A and matches all constraints. Similarly, a defined type can be assigned
 * to a record type so long as that defined type matches all property
 * constraints of the record type. A record type of the form { a : A, b : B }
 * can be assigned to a record of type { a : A }.
 *
 */
class RecordType_Test {
  private final JSTypeRegistry registry = new JSTypeRegistry();
  private final Map<String, RecordProperty> properties = Maps.newHashMap();
  private final RecordType recordType = new RecordType(registry, properties);

  @Test
  public void testIsEquivalentTo_sameType() {
    RecordType other = new RecordType(registry, properties);
    assertTrue(recordType.isEquivalentTo(other));
  }

  @Test
  public void testIsEquivalentTo_differentType() {
    Map<String, RecordProperty> otherProps = Maps.newHashMap();
    otherProps.put("x", new RecordProperty(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), null));
    RecordType other = new RecordType(registry, otherProps);
    assertFalse(recordType.isEquivalentTo(other));
  }

  @Test
  public void testIsSubtype_sameType() {
    assertTrue(recordType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtype_emptyObjectType() {
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertTrue(recordType.isSubtype(objectType));
  }

  @Test
  public void testIsSubtype_differentType() {
    Map<String, RecordProperty> otherProps = Maps.newHashMap();
    otherProps.put("x", new RecordProperty(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), null));
    RecordType other = new RecordType(registry, otherProps);
    assertFalse(recordType.isSubtype(other));
  }

  @Test
  public void testDefineProperty_notFrozen() {
    String propertyName = "x";
    JSType propertyType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    Node propertyNode = null;
    boolean inferred = false;

    boolean result = recordType.defineProperty(propertyName, propertyType, inferred, propertyNode);

    assertTrue(result);
    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(propertyType, recordType.getPropertyType(propertyName));
  }

  @Test
  public void testDefineProperty_frozen() {
    String propertyName = "x";
    JSType propertyType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    Node propertyNode = null;
    boolean inferred = false;

    recordType.setFrozen(true);
    boolean result = recordType.defineProperty(propertyName, propertyType, inferred, propertyNode);

    assertFalse(result);
    assertFalse(recordType.hasProperty(propertyName));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType other = new RecordType(registry, properties);

    JSType result = recordType.getGreatestSubtypeHelper(other);

    assertEquals(recordType, result);
  }

  @Test
  public void testToMaybeRecordType() {
    RecordType result = recordType.toMaybeRecordType();

    assertEquals(recordType, result);
  }
}