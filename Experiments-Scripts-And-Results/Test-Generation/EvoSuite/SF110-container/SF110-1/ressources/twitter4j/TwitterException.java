package twitter4j;

import twitter4j.internal.http.*;
import java.io.*;

public class TwitterException extends Exception implements TwitterResponse, HttpResponseCode
{
    private int statusCode;
    private int retryAfter;
    private RateLimitStatus rateLimitStatus;
    private RateLimitStatus featureSpecificRateLimitStatus;
    private static final long serialVersionUID = -2623309261327598087L;
    private static final String[] FILTER;
    boolean nested;
    
    public TwitterException(final String msg) {
        super(msg);
        this.statusCode = -1;
        this.featureSpecificRateLimitStatus = null;
        this.nested = false;
        this.rateLimitStatus = null;
    }
    
    public TwitterException(final Exception cause) {
        super(cause);
        this.statusCode = -1;
        this.featureSpecificRateLimitStatus = null;
        this.nested = false;
        if (cause instanceof TwitterException) {
            ((TwitterException)cause).setNested();
        }
        this.rateLimitStatus = null;
    }
    
    public TwitterException(final String msg, final HttpResponse res) {
        super(getCause(res) + "\n" + msg);
        this.statusCode = -1;
        this.featureSpecificRateLimitStatus = null;
        this.nested = false;
        if (res.getStatusCode() == 420) {
            try {
                final String retryAfterStr = res.getResponseHeader("Retry-After");
                if (null != retryAfterStr) {
                    this.retryAfter = Integer.valueOf(retryAfterStr);
                }
            }
            catch (NumberFormatException ignore) {
                this.retryAfter = -1;
            }
        }
        this.statusCode = res.getStatusCode();
        this.rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res);
        this.featureSpecificRateLimitStatus = RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(res);
    }
    
    public TwitterException(final String msg, final Exception cause) {
        super(msg, cause);
        this.statusCode = -1;
        this.featureSpecificRateLimitStatus = null;
        this.nested = false;
    }
    
    public TwitterException(final String msg, final Exception cause, final int statusCode) {
        super(msg, cause);
        this.statusCode = -1;
        this.featureSpecificRateLimitStatus = null;
        this.nested = false;
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return this.statusCode;
    }
    
    public RateLimitStatus getRateLimitStatus() {
        return this.rateLimitStatus;
    }
    
    public RateLimitStatus getFeatureSpecificRateLimitStatus() {
        return this.featureSpecificRateLimitStatus;
    }
    
    public int getRetryAfter() {
        if (this.statusCode != 420) {
            throw new IllegalStateException("Rate limitation is not exceeded");
        }
        return this.retryAfter;
    }
    
    public boolean isCausedByNetworkIssue() {
        return this.getCause() instanceof IOException;
    }
    
    public boolean exceededRateLimitation() {
        return (this.statusCode == 400 && null != this.rateLimitStatus) || this.statusCode == 420;
    }
    
    public boolean resourceNotFound() {
        return this.statusCode == 404;
    }
    
    public String getExceptionCode() {
        return new ExceptionDiagnosis(this, TwitterException.FILTER).asHexString();
    }
    
    void setNested() {
        this.nested = true;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final TwitterException that = (TwitterException)o;
        if (this.retryAfter != that.retryAfter) {
            return false;
        }
        if (this.statusCode != that.statusCode) {
            return false;
        }
        if (this.rateLimitStatus != null) {
            if (this.rateLimitStatus.equals(that.rateLimitStatus)) {
                return true;
            }
        }
        else if (that.rateLimitStatus == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        int result = this.statusCode;
        result = 31 * result + this.retryAfter;
        result = 31 * result + ((this.rateLimitStatus != null) ? this.rateLimitStatus.hashCode() : 0);
        return result;
    }
    
    public String toString() {
        return "TwitterException{" + (this.nested ? "" : ("exceptionCode=[" + this.getExceptionCode() + "], ")) + "statusCode=" + this.statusCode + ", retryAfter=" + this.retryAfter + ", rateLimitStatus=" + this.rateLimitStatus + ", version=" + Version.getVersion() + '}';
    }
    
    private static String getCause(final HttpResponse res) {
        final int statusCode = res.getStatusCode();
        String cause = null;
        switch (statusCode) {
            case 304: {
                cause = "There was no new data to return.";
                break;
            }
            case 400: {
                cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
                break;
            }
            case 401: {
                cause = "Authentication credentials were missing or incorrect.";
                break;
            }
            case 403: {
                cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
                break;
            }
            case 404: {
                cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
                break;
            }
            case 406: {
                cause = "Returned by the Search API when an invalid format is specified in the request.";
                break;
            }
            case 420: {
                cause = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
                break;
            }
            case 500: {
                cause = "Something is broken.  Please post to the group so the Twitter team can investigate.";
                break;
            }
            case 502: {
                cause = "Twitter is down or being upgraded.";
                break;
            }
            case 503: {
                cause = "Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
                break;
            }
            default: {
                cause = "";
                break;
            }
        }
        return statusCode + ":" + cause;
    }
    
    static {
        FILTER = new String[] { "twitter4j" };
    }
}
