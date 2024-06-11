package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;

class DefaultPhdReadTag implements PhdReadTag
{
    private final String type;
    private final String source;
    private final Range ungappedRange;
    private final Date date;
    private final String comment;
    private final String freeFormData;
    
    public DefaultPhdReadTag(final String type, final String source, final Range ungappedRange, final Date date, final String comment, final String freeFormData) {
        this.type = type;
        this.source = source;
        this.ungappedRange = ungappedRange;
        this.date = date;
        this.comment = comment;
        this.freeFormData = freeFormData;
    }
    
    @Override
    public final String getType() {
        return this.type;
    }
    
    @Override
    public final String getSource() {
        return this.source;
    }
    
    @Override
    public final Range getUngappedRange() {
        return this.ungappedRange;
    }
    
    @Override
    public final Date getDate() {
        return this.date;
    }
    
    @Override
    public final String getComment() {
        return this.comment;
    }
    
    @Override
    public final String getFreeFormData() {
        return this.freeFormData;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.comment == null) ? 0 : this.comment.hashCode());
        result = 31 * result + ((this.date == null) ? 0 : this.date.hashCode());
        result = 31 * result + ((this.freeFormData == null) ? 0 : this.freeFormData.hashCode());
        result = 31 * result + ((this.source == null) ? 0 : this.source.hashCode());
        result = 31 * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = 31 * result + ((this.ungappedRange == null) ? 0 : this.ungappedRange.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DefaultPhdReadTag)) {
            return false;
        }
        final DefaultPhdReadTag other = (DefaultPhdReadTag)obj;
        if (this.comment == null) {
            if (other.comment != null) {
                return false;
            }
        }
        else if (!this.comment.equals(other.comment)) {
            return false;
        }
        if (this.date == null) {
            if (other.date != null) {
                return false;
            }
        }
        else if (!this.date.equals(other.date)) {
            return false;
        }
        if (this.freeFormData == null) {
            if (other.freeFormData != null) {
                return false;
            }
        }
        else if (!this.freeFormData.equals(other.freeFormData)) {
            return false;
        }
        if (this.source == null) {
            if (other.source != null) {
                return false;
            }
        }
        else if (!this.source.equals(other.source)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        }
        else if (!this.type.equals(other.type)) {
            return false;
        }
        if (this.ungappedRange == null) {
            if (other.ungappedRange != null) {
                return false;
            }
        }
        else if (!this.ungappedRange.equals(other.ungappedRange)) {
            return false;
        }
        return true;
    }
}
