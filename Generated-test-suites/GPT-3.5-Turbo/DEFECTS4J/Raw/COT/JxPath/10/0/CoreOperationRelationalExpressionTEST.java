/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression coreOpRelationalExp;

    @Before
    public void setup() {
        coreOpRelationalExp = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue() {
        EvalContext evalContext = new EvalContext();
        evalContext.setSingleNodePointer("Test");

        Object result = coreOpRelationalExp.computeValue(evalContext);

//        perform assertions as per your requirements
    }

    @Test
    public void testGetPrecedence() {
        int precedence = coreOpRelationalExp.getPrecedence();

        // perform assertions as per your requirements
    }

    @Test
    public void testIsSymmetric() {
        boolean isSymmetric = coreOpRelationalExp.isSymmetric();

        // perform assertions as per your requirements
    }

    @Test
    public void testEvaluateCompare() {
        boolean result = coreOpRelationalExp.evaluateCompare(1);

        // perform assertions as per your requirements
    }

    @Test
    public void testContainsMatch() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        boolean containsMatch = coreOpRelationalExp.containsMatch(list.iterator(), 3);

        // perform assertions as per your requirements
    }

    @Test
    public void testFindMatch() {
        List<Integer> leftList = Arrays.asList(1, 2, 3);
        List<Integer> rightList = Arrays.asList(3, 4, 5);
        boolean containsMatch = coreOpRelationalExp.findMatch(leftList.iterator(), rightList.iterator());

        // perform assertions as per your requirements
    }

    @Test
    public void testCompute() {
        Object left = 1;
        Object right = 2;
        boolean result = coreOpRelationalExp.compute(left, right);

        // perform assertions as per your requirements
    }
}