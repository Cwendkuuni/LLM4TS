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
 * ***** END LICENSE BLOCK **** */

package com.google.javascript.rhino.jstype;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  RecordType recordType;

  @Before
  public void setUp() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.OBJECT_TYPE), null));
    properties.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
    properties.put("c", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testGetPropertyType() {
    assertEquals(recordType.getPropertyType("a"), registry.getNativeType(JSTypeNative.OBJECT_TYPE));
    assertEquals(recordType.getPropertyType("b"), registry.getNativeType(JSTypeNative.STRING_TYPE));
    assertEquals(recordType.getPropertyType("c"), registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    assertNull(recordType.getPropertyType("d"));
  }

  @Test
  public void testHasProperty() {
    assertTrue(recordType.hasProperty("a"));
    assertTrue(recordType.hasProperty("b"));
    assertTrue(recordType.hasProperty("c"));
    assertFalse(recordType.hasProperty("d"));
  }

  @Test
  public void testIsSubtype() {
    assertTrue(recordType.isSubtype(registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
    assertTrue(recordType.isSubtype(recordType));
    assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.NO_TYPE)));
  }

  @Test
  public void testIsEquivalentTo() {
    assertTrue(recordType.isEquivalentTo(recordType));
    assertFalse(recordType.isEquivalentTo(registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter(null);
    StaticScope<JSType> scope = new StaticScope<JSType>();
    recordType.resolveInternal(t, scope);
  }

}