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

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;

public class RecordType extends PrototypeObjectType {
  private static final long serialVersionUID = 1L;

  private final boolean declared;
  private boolean isFrozen = false;

  RecordType(JSTypeRegistry registry, Map<String, RecordProperty> properties) {
    this(registry, properties, true);
  }

  RecordType(JSTypeRegistry registry, Map<String, RecordProperty> properties,
      boolean declared) {
    super(registry, null, null);
    setPrettyPrint(true);
    this.declared = declared;

    for (String property : properties.keySet()) {
      RecordProperty prop = properties.get(property);
      if (prop == null) {
        throw new IllegalStateException(
            "RecordProperty associated with a property should not be null!");
      }
      if (declared) {
        defineDeclaredProperty(
            property, prop.getType(), prop.getPropertyNode());
      } else {
        defineSynthesizedProperty(
            property, prop.getType(), prop.getPropertyNode());
      }
    }

    // Freeze the record type.
    isFrozen = true;
  }

  /** @return Is this synthesized for internal bookkeeping? */
  boolean isSynthetic() {
    return !declared;
  }

  boolean checkRecordEquivalenceHelper(
      RecordType otherRecord, EquivalenceMethod eqMethod) {
    Set<String> keySet = getOwnPropertyNames();
    Set<String> otherKeySet = otherRecord.getOwnPropertyNames();
    if (!otherKeySet.equals(keySet)) {
      return false;
    }
    for (String key : keySet) {
      if (!otherRecord.getPropertyType(key).checkEquivalenceHelper(
              getPropertyType(key), eqMethod)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ObjectType getImplicitPrototype() {
    return registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
  }

  @Override
  boolean defineProperty(String propertyName, JSType type,
      boolean inferred, Node propertyNode) {
    if (isFrozen) {
      return false;
    }

    return super.defineProperty(propertyName, type, inferred,
        propertyNode);
  }

  JSType getGreatestSubtypeHelper(JSType that) {
    if (that.isRecordType()) {
      RecordType thatRecord = that.toMaybeRecordType();
      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      builder.setSynthesized(true);

      for (String property : getOwnPropertyNames()) {
        if (thatRecord.hasProperty(property) &&
            !thatRecord.getPropertyType(property).isInvariant(
                getPropertyType(property))) {
          return registry.getNativeObjectType(JSTypeNative.NO_TYPE);
        }

        builder.addProperty(property, getPropertyType(property),
            getPropertyNode(property));
      }

      for (String property : thatRecord.getOwnPropertyNames()) {
        if (!hasProperty(property)) {
          builder.addProperty(property, thatRecord.getPropertyType(property),
              thatRecord.getPropertyNode(property));
        }
      }

      return builder.build();
    }

    JSType greatestSubtype = registry.getNativeType(
        JSTypeNative.NO_OBJECT_TYPE);
    JSType thatRestrictedToObj =
        registry.getNativeType(JSTypeNative.OBJECT_TYPE)
        .getGreatestSubtype(that);
    if (!thatRestrictedToObj.isEmptyType()) {

      for (String propName : getOwnPropertyNames()) {
        JSType propType = getPropertyType(propName);
        UnionTypeBuilder builder = new UnionTypeBuilder(registry);
        for (ObjectType alt :
                 registry.getEachReferenceTypeWithProperty(propName)) {
          JSType altPropType = alt.getPropertyType(propName);
          if (altPropType != null && !alt.isEquivalentTo(this) &&
              alt.isSubtype(that) &&
              propType.isInvariant(altPropType)) {
            builder.addAlternate(alt);
          }
        }
        greatestSubtype = greatestSubtype.getLeastSupertype(builder.build());
      }
    }
    return greatestSubtype;
  }

  @Override
  RecordType toMaybeRecordType() {
    return this;
  }

  @Override
  public boolean isSubtype(JSType that) {
    if (JSType.isSubtypeHelper(this, that)) {
      return true;
    }

    // Top of the record types is the empty record, or OBJECT_TYPE.
    if (registry.getNativeObjectType(
            JSTypeNative.OBJECT_TYPE).isSubtype(that)) {
      return true;
    }

    // A type is a subtype of a record type if it itself is a record
    // type and it has at least the same members as the parent record type
    // with the same types.
    if (!that.isRecordType()) {
      return false;
    }

    return RecordType.isSubtype(this, that.toMaybeRecordType());
  }

  /** Determines if typeA is a subtype of typeB */
  static boolean isSubtype(ObjectType typeA, RecordType typeB) {
    for (String property : typeB.getOwnPropertyNames()) {
      if (!typeA.hasProperty(property)) {
        return false;
      }

      JSType propA = typeA.getPropertyType(property);
      JSType propB = typeB.getPropertyType(property);
      if (typeA.isPropertyTypeDeclared(property)) {
        // If one declared property isn't invariant,
        // then the whole record isn't covariant.
        if (!propA.isInvariant(propB)) {
          return false;
        }
      } else {

        if (!propA.isSubtype(propB)) {
          return false;
        }
      }
    }

    return true;
  }
}
