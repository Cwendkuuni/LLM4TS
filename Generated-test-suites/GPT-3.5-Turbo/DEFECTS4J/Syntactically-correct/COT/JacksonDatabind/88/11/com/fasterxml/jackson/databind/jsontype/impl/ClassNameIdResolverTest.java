package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * {@link com.fasterxml.jackson.databind.jsontype.TypeIdResolver} implementation
 * that converts between fully-qualified
 * Java class names and (JSON) Strings.
 */
public class ClassNameIdResolver
    extends TypeIdResolverBase
{
    public ClassNameIdResolver(JavaType baseType, TypeFactory typeFactory) {
        super(baseType, typeFactory);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() { return JsonTypeInfo.Id.CLASS; }

    // Rest of the class code omitted for brevity

}