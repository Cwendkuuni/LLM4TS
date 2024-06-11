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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.type.InferenceException;
import com.google.javascript.rhino.testing.Asserts;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.junit.Test;

/**
 * A test class for the RecordType class.
 */
public class RecordTypeTest {

  private final JSTypeRegistry registry = new JSTypeRegistry(null, null);

  @Test
  public void testIsSubtype() {
    RecordType recordA = createRecordType("a", JSTypeNative.STRING_TYPE);
    RecordType recordB = createRecordType("a", JSTypeNative.STRING_TYPE);
    assertTrue(recordA.isSubtype(recordB));
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordA = createRecordType("a", JSTypeNative.NUMBER_TYPE);
    RecordType recordB = createRecordType("a", JSTypeNative.STRING_TYPE);
    RecordType recordC = createRecordType("a", JSTypeNative.NUMBER_TYPE);
    assertFalse(recordA.checkRecordEquivalenceHelper(recordB, false));
    assertTrue(recordA.checkRecordEquivalenceHelper(recordC, false));
  }

  @Test
  public void testSubtyping() {
    RecordType recordB = createRecordType("b", JSTypeNative.OBJECT_TYPE);
    RecordType recordC = createRecordType("a", JSTypeNative.STRING_TYPE);
    UnionType union =
        new UnionTypeBuilder(registry).addAlternate(recordC).addAlternate(recordB).build();
    ObjectType objectType = registry.createAnonymousObjectType();
    assertTrue(recordB.isSubtype(union));
    assertFalse(recordC.isSubtype(union));
    assertFalse(objectType.isSubtype(recordB));
  }

  @Test
  public void testGreatestSubtypeHelper() {
    RecordType recordTypeA = createRecordType("a", JSTypeNative.STRING_TYPE);
    assertNull(recordTypeA.getGreatestSubtypeHelper(JSTypeNative.STRING_TYPE));
  }

  @Test
  public void testResolveInternal() {
    RecordType recordType = createRecordType("a", JSTypeNative.UNKNOWN_TYPE);
    ErrorReporter reporter = new ErrorReporter(null);
    recordType.resolveInternal(reporter, null);
    assertEquals(JSTypeNative.NO_TYPE, recordType.getPropertyType("a"));
  }

  private RecordType createRecordType(String name, JSType type) {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put(name, new RecordProperty(type, null));
    return new RecordType(registry, properties);
  }
}