package org.jdom2.util;

import org.jdom2.internal.*;
import org.jdom2.*;
import java.util.*;

public final class NamespaceStack implements Iterable<Namespace>
{
    private static final Namespace[] EMPTY;
    private static final Iterable<Namespace> EMPTYITER;
    private static final Comparator<Namespace> NSCOMP;
    private static final Namespace[] DEFAULTSEED;
    private Namespace[][] added;
    private Namespace[][] scope;
    private int depth;
    
    private static final int binarySearch(final Namespace[] data, int left, int right, final Namespace key) {
        --right;
        while (left <= right) {
            final int mid = left + right >>> 1;
            if (data[mid] == key) {
                return mid;
            }
            final int cmp = NamespaceStack.NSCOMP.compare(data[mid], key);
            if (cmp < 0) {
                left = mid + 1;
            }
            else {
                if (cmp <= 0) {
                    return mid;
                }
                right = mid - 1;
            }
        }
        return -left - 1;
    }
    
    public NamespaceStack() {
        this(NamespaceStack.DEFAULTSEED);
    }
    
    public NamespaceStack(final Namespace[] seed) {
        this.added = new Namespace[10][];
        this.scope = new Namespace[10][];
        this.depth = -1;
        ++this.depth;
        this.added[this.depth] = seed;
        this.scope[this.depth] = this.added[this.depth];
    }
    
    private static final Namespace[] checkNamespace(final List<Namespace> store, final Namespace namespace, final Namespace[] scope) {
        if (namespace == scope[0]) {
            return scope;
        }
        if (namespace.getPrefix().equals(scope[0].getPrefix())) {
            store.add(namespace);
            final Namespace[] nscope = ArrayCopy.copyOf(scope, scope.length);
            nscope[0] = namespace;
            return nscope;
        }
        int ip = binarySearch(scope, 1, scope.length, namespace);
        if (ip >= 0 && namespace == scope[ip]) {
            return scope;
        }
        store.add(namespace);
        if (ip >= 0) {
            final Namespace[] nscope2 = ArrayCopy.copyOf(scope, scope.length);
            nscope2[ip] = namespace;
            return nscope2;
        }
        final Namespace[] nscope2 = ArrayCopy.copyOf(scope, scope.length + 1);
        ip = -ip - 1;
        System.arraycopy(nscope2, ip, nscope2, ip + 1, nscope2.length - ip - 1);
        nscope2[ip] = namespace;
        return nscope2;
    }
    
    public void push(final Element element) {
        final List<Namespace> toadd = new ArrayList<Namespace>(8);
        final Namespace mns = element.getNamespace();
        Namespace[] newscope = checkNamespace(toadd, mns, this.scope[this.depth]);
        if (element.hasAdditionalNamespaces()) {
            for (final Namespace ns : element.getAdditionalNamespaces()) {
                if (ns == mns) {
                    continue;
                }
                newscope = checkNamespace(toadd, ns, newscope);
            }
        }
        if (element.hasAttributes()) {
            for (final Attribute a : element.getAttributes()) {
                final Namespace ns2 = a.getNamespace();
                if (ns2 == Namespace.NO_NAMESPACE) {
                    continue;
                }
                if (ns2 == mns) {
                    continue;
                }
                newscope = checkNamespace(toadd, ns2, newscope);
            }
        }
        this.pushStack(mns, newscope, toadd);
    }
    
    public void push(final Attribute att) {
        final List<Namespace> toadd = new ArrayList<Namespace>(1);
        final Namespace mns = att.getNamespace();
        final Namespace[] newscope = checkNamespace(toadd, mns, this.scope[this.depth]);
        this.pushStack(mns, newscope, toadd);
    }
    
    private final void pushStack(final Namespace mns, Namespace[] newscope, final List<Namespace> toadd) {
        ++this.depth;
        if (this.depth >= this.scope.length) {
            this.scope = ArrayCopy.copyOf(this.scope, this.scope.length * 2);
            this.added = ArrayCopy.copyOf(this.added, this.scope.length);
        }
        if (toadd.isEmpty()) {
            this.added[this.depth] = NamespaceStack.EMPTY;
        }
        else {
            this.added[this.depth] = toadd.toArray(new Namespace[toadd.size()]);
            if (this.added[this.depth][0] == mns) {
                Arrays.sort(this.added[this.depth], 1, this.added[this.depth].length, NamespaceStack.NSCOMP);
            }
            else {
                Arrays.sort(this.added[this.depth], NamespaceStack.NSCOMP);
            }
        }
        if (mns != newscope[0]) {
            if (toadd.isEmpty()) {
                newscope = ArrayCopy.copyOf(newscope, newscope.length);
            }
            final Namespace tmp = newscope[0];
            int ip = -binarySearch(newscope, 1, newscope.length, tmp) - 1;
            --ip;
            System.arraycopy(newscope, 1, newscope, 0, ip);
            newscope[ip] = tmp;
            ip = binarySearch(newscope, 0, newscope.length, mns);
            System.arraycopy(newscope, 0, newscope, 1, ip);
            newscope[0] = mns;
        }
        this.scope[this.depth] = newscope;
    }
    
    public void pop() {
        if (this.depth <= 0) {
            throw new IllegalStateException("Cannot over-pop the stack.");
        }
        this.scope[this.depth] = null;
        this.added[this.depth] = null;
        --this.depth;
    }
    
    public Iterable<Namespace> addedForward() {
        if (this.added[this.depth].length == 0) {
            return NamespaceStack.EMPTYITER;
        }
        return new NamespaceIterable(this.added[this.depth], true);
    }
    
    public Iterable<Namespace> addedReverse() {
        if (this.added[this.depth].length == 0) {
            return NamespaceStack.EMPTYITER;
        }
        return new NamespaceIterable(this.added[this.depth], false);
    }
    
    public Iterator<Namespace> iterator() {
        return new ForwardWalker(this.scope[this.depth]);
    }
    
    public Namespace[] getScope() {
        return ArrayCopy.copyOf(this.scope[this.depth], this.scope[this.depth].length);
    }
    
    public boolean isInScope(final Namespace ns) {
        if (ns == this.scope[this.depth][0]) {
            return true;
        }
        final int ip = binarySearch(this.scope[this.depth], 1, this.scope[this.depth].length, ns);
        return ip >= 0 && ns == this.scope[this.depth][ip];
    }
    
    static {
        EMPTY = new Namespace[0];
        EMPTYITER = new EmptyIterable();
        NSCOMP = new Comparator<Namespace>() {
            public int compare(final Namespace ns1, final Namespace ns2) {
                return ns1.getPrefix().compareTo(ns2.getPrefix());
            }
        };
        DEFAULTSEED = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
    }
    
    private static final class ForwardWalker implements Iterator<Namespace>
    {
        private final Namespace[] namespaces;
        int cursor;
        
        public ForwardWalker(final Namespace[] namespaces) {
            this.cursor = 0;
            this.namespaces = namespaces;
        }
        
        public boolean hasNext() {
            return this.cursor < this.namespaces.length;
        }
        
        public Namespace next() {
            if (this.cursor >= this.namespaces.length) {
                throw new NoSuchElementException("Cannot over-iterate...");
            }
            return this.namespaces[this.cursor++];
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove Namespaces from iterator");
        }
    }
    
    private static final class BackwardWalker implements Iterator<Namespace>
    {
        private final Namespace[] namespaces;
        int cursor;
        
        public BackwardWalker(final Namespace[] namespaces) {
            this.cursor = -1;
            this.namespaces = namespaces;
            this.cursor = namespaces.length - 1;
        }
        
        public boolean hasNext() {
            return this.cursor >= 0;
        }
        
        public Namespace next() {
            if (this.cursor < 0) {
                throw new NoSuchElementException("Cannot over-iterate...");
            }
            return this.namespaces[this.cursor--];
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove Namespaces from iterator");
        }
    }
    
    private static final class NamespaceIterable implements Iterable<Namespace>
    {
        private final boolean forward;
        private final Namespace[] namespaces;
        
        public NamespaceIterable(final Namespace[] data, final boolean forward) {
            this.forward = forward;
            this.namespaces = data;
        }
        
        public Iterator<Namespace> iterator() {
            return this.forward ? new ForwardWalker(this.namespaces) : new BackwardWalker(this.namespaces);
        }
    }
    
    private static final class EmptyIterable implements Iterable<Namespace>, Iterator<Namespace>
    {
        public Iterator<Namespace> iterator() {
            return this;
        }
        
        public boolean hasNext() {
            return false;
        }
        
        public Namespace next() {
            throw new NoSuchElementException("Can not call next() on an empty Iterator.");
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove Namespaces from iterator");
        }
    }
}
