/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        double delta = 0.000001;
        assertEquals(0.5723649429247001, Gamma.logGamma(1), delta);
        assertEquals(2.251752589066721, Gamma.logGamma(2), delta);
        assertEquals(4.787491742782046, Gamma.logGamma(5), delta);
        assertEquals(8.525161361065414, Gamma.logGamma(10), delta);
    }

    @Test
    public void testRegularizedGammaP() {
        double delta = 0.000001;
        assertEquals(0, Gamma.regularizedGammaP(1, 0), delta);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(1, 1), delta);
        assertEquals(0.9999999998767209, Gamma.regularizedGammaP(1, 10), delta);
        assertEquals(0.9999999999999998, Gamma.regularizedGammaP(1, 100), delta);
        
        assertEquals(0, Gamma.regularizedGammaP(2, 0), delta);
        assertEquals(0.26424111765711534, Gamma.regularizedGammaP(2, 1), delta);
        assertEquals(0.9707477246681527, Gamma.regularizedGammaP(2, 10), delta);
        assertEquals(1, Gamma.regularizedGammaP(2, 100), delta);
        
        assertEquals(0, Gamma.regularizedGammaP(5, 0), delta);
        assertEquals(0.0013893129772905885, Gamma.regularizedGammaP(5, 1), delta);
        assertEquals(0.7678224655082374, Gamma.regularizedGammaP(5, 10), delta);
        assertEquals(1, Gamma.regularizedGammaP(5, 100), delta);
        
        assertEquals(0, Gamma.regularizedGammaP(10, 0), delta);
        assertEquals(1.5353167642672053E-06, Gamma.regularizedGammaP(10, 1), delta);
        assertEquals(0.999999900740996, Gamma.regularizedGammaP(10, 10), delta);
        assertEquals(1, Gamma.regularizedGammaP(10, 100), delta);
    }
    
    @Test
    public void testRegularizedGammaQ() {
        double delta = 0.000001;
        assertEquals(1, Gamma.regularizedGammaQ(1, 0), delta);
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(1, 1), delta);
        assertEquals(1.2375952779475154E-10, Gamma.regularizedGammaQ(1, 10), delta);
        assertEquals(2.0813055967745867E-88, Gamma.regularizedGammaQ(1, 100), delta);
        
        assertEquals(1, Gamma.regularizedGammaQ(2, 0), delta);
        assertEquals(0.7357588823428847, Gamma.regularizedGammaQ(2, 1), delta);
        assertEquals(2.9252275338161886E-06, Gamma.regularizedGammaQ(2, 10), delta);
        assertEquals(2.612295080915986E-29, Gamma.regularizedGammaQ(2, 100), delta);
        
        assertEquals(1, Gamma.regularizedGammaQ(5, 0), delta);
        assertEquals(0.9986106870227094, Gamma.regularizedGammaQ(5, 1), delta);
        assertEquals(0.23217753449176258, Gamma.regularizedGammaQ(5, 10), delta);
        assertEquals(1.4264903293306878E-56, Gamma.regularizedGammaQ(5, 100), delta);
        
        assertEquals(1, Gamma.regularizedGammaQ(10, 0), delta);
        assertEquals(0.9999984646832357, Gamma.regularizedGammaQ(10, 1), delta);
        assertEquals(9.259003900351214E-08, Gamma.regularizedGammaQ(10, 10), delta);
        assertEquals(8.099259003508977E-46, Gamma.regularizedGammaQ(10, 100), delta);
    }
}