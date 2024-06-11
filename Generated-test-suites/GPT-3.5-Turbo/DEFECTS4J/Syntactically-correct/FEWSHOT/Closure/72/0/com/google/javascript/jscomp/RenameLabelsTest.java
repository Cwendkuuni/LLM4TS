/*
  * Copyright 2008 The Closure Compiler Authors.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

 package com.google.javascript.jscomp;

 import com.google.common.base.Preconditions;
 import com.google.common.base.Supplier;
 import com.google.common.collect.Lists;
 import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
 import com.google.javascript.rhino.Node;
 import com.google.javascript.rhino.Token;

 import java.util.ArrayList;
 import java.util.Deque;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Map;


 /**
  * RenameLabels renames all the labels so that they have short names, to reduce
  * code size and also to obfuscate the code.
  *
  * Label names have a unique namespace, so variable or function names clashes
  * are not a concern, but keywords clashes are.
  *
  * Additionally, labels names are only within the statements include in the
  * label and do not cross function boundaries. This means that it is possible to
  * create one label name that is used for labels at any given depth of label
  * nesting. Typically, the name "a" will be used for all top level labels, "b"
  * for the next nested label, and so on. For example:
  *
  * <code>
  * function bar() {
  *   a: {
  *     b: {
  *       foo();
  *     }
  *   }
  *
  *   a: {
  *     b: break a;
  *   }
  * }
  * </code>
  *
  * The general processes is as follows: process() is the entry point for the
  * CompilerPass, and from there a standard "ScopedCallback" traversal is done,
  * where "shouldTraverse" is called when descending the tree, and the "visit" is
  * called in a depth first manner. The name for the label is selected during the
  * decent in "shouldTraverse", and the references to the label name are renamed
  * as they are encountered during the "visit". This means that if the label is
  * unreferenced, it is known when the label node is visited, and, if so, can be
  * safely removed.
  *
  * @author johnlenz@google.com (John Lenz)
  */
 final class RenameLabelsTest {
   private AbstractCompiler compiler;
   private Supplier<String> nameSupplier;
   private boolean removeUnused;

   @Test
   public void testProcess() {
     RenameLabels renameLabels = new RenameLabels(compiler, nameSupplier, removeUnused);
     renameLabels.process(new Node(), new Node());
   }

   @Test
   public void testProcessWithExterns() {
     RenameLabels renameLabels = new RenameLabels(compiler, nameSupplier, removeUnused);
     renameLabels.process(new Node(), new Node());
   }
 }