
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
package org.apache.commons.lang.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {
  
  @Test
  public void testStartStop() {
    StopWatch watch = new StopWatch();
    watch.start();
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    watch.stop();
    assertTrue(watch.getTime() >= 1000L);
  }
  
  @Test
  public void testSplit() {
    StopWatch watch = new StopWatch();
    watch.start();
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    watch.split();
    assertTrue(watch.getSplitTime() >= 1000L);
  }
  
  @Test
  public void testUnsplit() {
    StopWatch watch = new StopWatch();
    watch.start();
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    watch.split();
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    watch.unsplit();
    assertTrue(watch.getTime() >= 2000L);
  }
  
  @Test
  public void testResume() {
    StopWatch watch = new StopWatch();
    watch.start();
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    watch.suspend();
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    watch.resume();
    assertTrue(watch.getTime() >= 1000L);
  }
  
  @Test
  public void testReset() {
    StopWatch watch = new StopWatch();
    watch.start();
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    watch.reset();
    assertEquals(0L, watch.getTime());
  }

  @Test(expected=IllegalStateException.class)
  public void testStartWithoutReset() {
    StopWatch watch = new StopWatch();
    watch.start();
    watch.start();
  }
  
  @Test(expected=IllegalStateException.class)
  public void testStopWithoutStart() {
    StopWatch watch = new StopWatch();
    watch.stop();
  }
  
  @Test(expected=IllegalStateException.class)
  public void testUnsplitWithoutSplit() {
    StopWatch watch = new StopWatch();
    watch.unsplit();
  }
  
  @Test(expected=IllegalStateException.class)
  public void testResumeWithoutSuspend() {
    StopWatch watch = new StopWatch();
    watch.resume();
  }
  
  @Test(expected=IllegalStateException.class)
  public void testSplitWithoutStart() {
    StopWatch watch = new StopWatch();
    watch.split();
  }
  
  @Test(expected=IllegalStateException.class)
  public void testGetSplitTimeWithoutSplit() {
    StopWatch watch = new StopWatch();
    watch.getSplitTime();
  }
  
  @Test
  public void testToString() {
    StopWatch watch = new StopWatch();
    watch.start();
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    watch.stop();
    String timeString = watch.toString();
    assertTrue(timeString.matches("\\d{2}:\\d{2}:\\d{2}"));
  }
  
  @Test
  public void testToSplitString() {
    StopWatch watch = new StopWatch();
    watch.start();
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    watch.split();
    String splitTimeString = watch.toSplitString();
    assertTrue(splitTimeString.matches("\\d{2}:\\d{2}:\\d{2}"));
  }
  
}
