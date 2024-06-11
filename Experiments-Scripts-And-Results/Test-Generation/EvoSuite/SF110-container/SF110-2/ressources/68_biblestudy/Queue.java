package bible.util;

import java.util.*;

public class Queue
{
    public static final int NO_MAXIMUM = -1;
    private int numItems;
    private int maxNumItems;
    private int maxCapacity;
    private Node first;
    private Node last;
    
    public Queue() {
        this(-1);
    }
    
    public Queue(final int capacity) {
        this.first = null;
        this.last = null;
        this.numItems = 0;
        this.maxNumItems = 0;
        this.maxCapacity = capacity;
    }
    
    public boolean isEmpty() {
        return this.first == null;
    }
    
    public void enqueue(final Object someObject) {
        if (someObject != null) {
            final Node newNode = new Node(someObject);
            synchronized (this) {
                if (this.isEmpty()) {
                    this.first = newNode;
                    this.last = newNode;
                }
                else {
                    this.last.next = newNode;
                    this.last = newNode;
                }
                ++this.numItems;
                if (this.numItems > this.maxNumItems) {
                    this.maxNumItems = this.numItems;
                }
            }
        }
    }
    
    public synchronized Object dequeue() {
        Object obj = null;
        if (this.isEmpty()) {
            System.out.println("Cannot remove when queue is empty");
        }
        else if (this.first == this.last) {
            obj = this.first.value;
            this.first = null;
            this.last = null;
        }
        else {
            obj = this.first.value;
            this.first = this.first.next;
        }
        --this.numItems;
        return obj;
    }
    
    public synchronized int remove(final Object object) {
        final int originalNumItems = this.numItems;
        while (this.first != null && this.first.value.equals(object)) {
            this.first = this.first.next;
            --this.numItems;
        }
        if (this.first == null) {
            this.last = null;
        }
        else {
            Node current = this.first;
            while (current.next != null) {
                if (current.next.value.equals(object)) {
                    current.next = current.next.next;
                    --this.numItems;
                    if (current.next != null) {
                        continue;
                    }
                    this.last = current;
                }
                else {
                    current = current.next;
                }
            }
        }
        return originalNumItems - this.numItems;
    }
    
    public synchronized void refreshElement(final Object object) {
        this.remove(object);
        this.enqueue(object);
    }
    
    public int getNumberItems() {
        return this.numItems;
    }
    
    public int getPeakNumberItems() {
        return this.maxNumItems;
    }
    
    public Vector getObjects() {
        final Vector tempVector = new Vector(this.numItems, 10);
        for (Node i = this.first; i != null; i = i.next) {
            tempVector.addElement(i.value);
        }
        return tempVector;
    }
    
    public boolean maxCapacityExceeded() {
        return this.numItems > this.maxCapacity - 1 && this.maxCapacity != -1;
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + ":[numItems=" + this.numItems + ", maxNumItems=" + this.maxNumItems + ", maxCapacity=" + this.maxCapacity + ", getObjects()=" + this.getObjects() + "\r\n]";
    }
    
    class Node
    {
        Node next;
        Object value;
        
        public Node(final Object obj) {
            this.value = obj;
        }
    }
}
